/*
 * The MIT License
 *
 * Copyright 2017 Neel Patel.
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
package com.articles_hub.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.logging.*;
import java.util.concurrent.*;


/**
 *
 * @author Neel Patel
 */
public class LogService {

    private static final Logger LOG = Logger.getLogger("com.articles_hub");
    private static final int LOG_MAX_SIZE = 50;
    private static final int FLUSH_DELAY = 500;
    private static LogService service = new LogService();
    private final MyHandler handler;
    
    public static LogService getLogService(){
        return service;
    }
    
    private LogService(){
        handler = new MyHandler();
        LOG.addHandler(handler);
        // get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        // suppress the logging output to the console
        Logger rootLogger = Logger.getLogger("");
        
    }
    
    public Logs getListner(){
        System.out.println("listner");
        Logs out = new Logs();
        handler.addOut(out);
        return out;
    }
    
    private class MyHandler extends Handler{

        private final Logger LOG = Logger.getLogger(MyHandler.class.getName());
        private ScheduledExecutorService exeService = DefaultExecutorService.getExecutorService();
        private final List<String> cache = new ArrayList<>();
        private final Set<Logs> outs=new HashSet<>();
        
        public MyHandler(){
            ScheduledExecutorService service=DefaultExecutorService.getExecutorService();
            service.scheduleWithFixedDelay(this::flush, FLUSH_DELAY,
                      FLUSH_DELAY, TimeUnit.MILLISECONDS);
        }
        
        @Override
        public void publish(LogRecord record) {
            synchronized(cache){
                cache.add(record.getLevel().getName()+"["+LocalDateTime.now(ZoneId.of("Asia/Kolkata"))+"]: "+record.getMessage());
            }
        }

        @Override
        public void flush() {
            List<String> copy;
            synchronized(cache){
                copy=new ArrayList<>(cache);
                cache.clear();
            }
            Set<Logs> closedOuts=new HashSet<>();
            synchronized(outs){
                outs.parallelStream().peek(out->{
                    try{
//                        System.out.println("flush out");
                        copy.forEach(out::addLogs);
                    }catch(Exception e){
                        out.close();
                    }
                }).filter(x->x.isClosed()).peek(x->closedOuts.add(x)).count();
                if(closedOuts.isEmpty())
                    return;
                LOG.info("LogService, number of closed streams :- "+closedOuts.size());
                outs.removeAll(closedOuts);
            }
        }

        @Override
        public void close() throws SecurityException {
            synchronized(outs){
                outs.forEach(x->x.close());
                outs.clear();
            }
        }
        
        public void addOut(Logs out){
            synchronized(outs){
                outs.add(out);
            }
        }
    }
    
    public class Logs{
        private List<String> logs=new ArrayList<>();
        private boolean isClosed=false;
        Logs(){}
        public synchronized void addLogs(String log){
            logs.add(log);
            if(logs.size()>LOG_MAX_SIZE)
                close();
        }
        
        public synchronized List<String> readAll(){
            List<String> temp = new ArrayList<>(logs);
            logs.clear();
            return temp;
        }
        
        public void close(){
            isClosed=true;
        }
        public boolean isClosed(){
            return isClosed;
        }
    }
}
