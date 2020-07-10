package com.lux.generator.listeners;

import com.lux.generator.events.ConnectionEvent;

public interface DataPanelListener {
    public void setDataToTable(String command,String urlAddress);
    public void setDataToTable();
    public void getInputData(ConnectionEvent connectionEvent);
}
