/* Soot - a J*va Optimization Framework
 * Copyright (C) 2002, 2003 Ondrej Lhotak
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

package soot.jimple.paddle;
import soot.*;
import java.util.*;

/** Performs a pseudo-topological sort on the VarNodes in a PAG.
 * @author Ondrej Lhotak
 */

public class TopoSorter {
    /** Actually perform the topological sort on the PAG. */
    public void sort() {
        for( Iterator it = PaddleNumberers.v().contextVarNodeNumberer().iterator(); it.hasNext(); ) {
            dfsVisit( (ContextVarNode) it.next() );
        }
        visited = null;
    }
    public TopoSorter( AbsPAG pag, boolean ignoreTypes ) {
        this.pag = pag;
        this.ignoreTypes = ignoreTypes;
        //this.visited = new NumberedSet( pag.getVarNodeNumberer() );
        this.visited = new HashSet();
    }
    
    /* End of public methods. */
    /* End of package methods. */

    protected boolean ignoreTypes;
    protected AbsPAG pag;
    protected int nextFinishNumber = 1;
    protected HashSet visited;
    protected void dfsVisit( ContextVarNode n ) {
        if( visited.contains( n ) ) return;
        visited.add( n );
        for( Iterator succIt = pag.simpleLookup(n); succIt.hasNext(); ) {
            final ContextVarNode succ = (ContextVarNode) succIt.next();
            if( ignoreTypes 
            || PaddleScene.v().tm.castNeverFails(
                    n.getType(), succ.getType() ) ) {
                dfsVisit( succ );
            }
        }
        n.setFinishingNumber( nextFinishNumber++ );
    }
}



