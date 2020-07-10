package com.lux.generator.events;

import java.io.InputStream;
import java.util.EventObject;

public class ConnectionEvent extends EventObject {
    
    private static final long serialVersionUID = 1L;
    private final InputStream is;
    
    public ConnectionEvent(Object source,InputStream is) {
        super(source);
        this.is=is;

    }

    public InputStream getIs() {
        return is;
    }


}
