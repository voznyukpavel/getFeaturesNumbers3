package com.lux.generator.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.lux.generator.manager.DataManager;
import com.lux.generator.settings.manager.SettingGetter;

public class MainWindow {

    private static final String NAME = "Get Feature Number";
    private static final String SETTINGS = "Settings";
    private static final String RESULT_DATA = "Data";
    private static final int MIN_WINDOW_HEIGHT = 430;
    private static final int MIN_WINDOW_WIDTH = 720;
    private static final int WINDOW_HEIGHT = 530;
    private static final int WINDOW_WIDTH = 720;

    private Display display;
    private Shell shell;
    private TabFolder tabFolder;
    private SettingGetter settingGetter;
    private DataManager dataManager;

    public void open() {
        initShell();
        tabFolder = new TabFolder(shell, SWT.NONE);
        settingGetter = new SettingGetter();
        dataManager=new DataManager();
        initResultUI();
        initDataUI();
        openWindow();
    }

    private void initDataUI() {
        TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
        Composite dataComposite = new Composite(tabFolder, SWT.NONE);
        tabItem.setText(SETTINGS);
        tabItem.setControl(dataComposite);
        new DataPanel(dataComposite, settingGetter,dataManager);
    }

    private void initResultUI() {
        TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
        Composite resultComposite = new Composite(tabFolder, SWT.NONE);
        tabItem.setText(RESULT_DATA);
        tabItem.setControl(resultComposite);
        new ResultPanel(resultComposite, settingGetter,dataManager);
    }

    private void initShell() {
        display = new Display();
        shell = new Shell(display);
        shell.setText(NAME);
        shell.setMinimumSize(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT);
        shell.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        shell.setLayout(new FillLayout());
    }

    private void openWindow() {
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
}