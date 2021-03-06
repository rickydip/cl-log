/*
 * The MIT License
 *
 * Copyright 2013 maurizio.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.clever.Common.VEInfo;




/**
 * Describe the state of a particular VM
 *  (now is used only for INFN projects)
 * @author maurizio
 */
public class VEState {

    public enum STATE
{
    RUNNING,
    STOPPED,
    SUSPENDED,
    ABORTED,
    UNKNOWN
}

    
    public VEState(STATE state, String id, String name)
    {
        if(state != null)
            this.state = state;
        else
            this.state = STATE.UNKNOWN;
        this.id = id;
        this.name = name;
    }
    
    final STATE state;
    final String id;
    final String name;

    
    
    @Override
    public String toString()
    {
        if(name == null )
            return null;
        StringBuffer response = new StringBuffer(id==null?"":id).append(" NAME : ").append(name==null?"":name).append(" STATE : ");
        
        switch (state)
        {
            case RUNNING : 
                response.append("running");
                break;
            case STOPPED :
                response.append("stopped");
                break;
            case SUSPENDED :
                response.append("suspended");
                break;
            case ABORTED :
                response.append("aborted");
                break;
            case UNKNOWN :
                response.append("unknown");
                break;
        }
        return response.toString();
        
    }
    
    public STATE getState() {
        return state;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    
}
