/*
 * The MIT License
 *
 * Copyright 2014 riccardo.
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

package org.clever.Common.Logging;

import org.clever.Common.Communicator.Agent;
import org.clever.Common.Plugins.RunnerPlugin;

/**
 *
 * @author riccardo
 */
public interface LoggingPlugin extends RunnerPlugin{

public int creaFileConfigurazioneLog();
public int validaComponenteSW(String path);
public String assegnaFrammento(String componente_sw, int n_c_sw);
public String componiConfLog(String[] vett_ok,int n);
public String componiAppConf(String [] path,int n);
public String componiLogConf(String[] path,int n);
public String componirootLogConf(String[] path,int n);
public int validaFile(String path);
public void componiFile(String link_file_contenitore, String contenuto_da_appendere);
public String fileToString(String path);
public void creaDir(String directoryName);
public void deleteFile(String file);

public void setOwner(Agent owner);    
    

}
