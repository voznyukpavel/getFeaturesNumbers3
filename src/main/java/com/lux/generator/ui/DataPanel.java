package com.lux.generator.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.json.simple.parser.ParseException;

import com.lux.generator.listeners.SettingsListener;
import com.lux.generator.manager.DataManager;
import com.lux.generator.net.Connector;
import com.lux.generator.settings.manager.SettingGetter;

class DataPanel implements SettingsListener {

    private static final String SETTINS_FILE = "settings.json";

    private static final String CONNECTION_SETTINGS = "Connection:";
    private static final String LOGIN = "Login:";
    private static final String PASSWORD = "Password:";

    private static final String USE_PROXY = "Use proxy";
    private static final String PROXY_HOST = "Proxy host:";
    private static final String PROXY_PORT = "Proxy port:";
    private static final String PROXY_USER = "Proxy user:";
    private static final String PROXY_PASSWORD = "Proxy password:";
    private static final String PROXY_SETTINGS = "Proxy:";

    private static final String SAVE_SETTINGS = "Save settings";

    private static final String IO_EXCEPTION = "IO Exception";
    private static final String PARSE_ERROR = "Parsing error";

    private Text loginText, passwordText, proxyHostText, proxyPortText, proxyUserText, proxyPasswordText;
    private Label proxyHostLabel, proxyPortLabel, proxyUserLabel, proxyPasswordLabel, proxySettingsLabel;
    private Button useProxyCheckbox, saveButton;
    private final Shell shell;
    private InputStream inputData;
    private final SettingGetter settingGetter;
    private final DataManager dataManager;

    public DataPanel(Composite dataComposite, SettingGetter settingGetter, DataManager dataManager) {
        dataComposite.setLayout(new GridLayout(3, false));
        shell = dataComposite.getShell();
        this.settingGetter = settingGetter;
        this.dataManager = dataManager;
        initUI(dataComposite);
        initListeners();
        signUpToTable();
    }

    private void initUI(Composite parent) {
        GridData checkBoxGridData = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
        checkBoxGridData.horizontalSpan = 3;

        Label connectionSettingsLabel = new Label(parent, SWT.BEGINNING);
        connectionSettingsLabel.setText(CONNECTION_SETTINGS);
        connectionSettingsLabel.setLayoutData(checkBoxGridData);

        FontData fontData = connectionSettingsLabel.getFont().getFontData()[0];
        Font font = new Font(null, new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));
        connectionSettingsLabel.setFont(font);

        GridData labelGridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
        labelGridData.widthHint = 85;

        GridData addressTextGridData = new GridData(GridData.FILL, GridData.FILL, true, false);

        addressTextGridData.horizontalSpan = 2;

        Label loginLabel = new Label(parent, SWT.BEGINNING);
        loginLabel.setText(LOGIN);
        loginLabel.setLayoutData(labelGridData);

        GridData logPassGridData = new GridData(GridData.BEGINNING, GridData.CENTER, false, false);
        logPassGridData.widthHint = 150;
        logPassGridData.horizontalSpan = 2;

        loginText = new Text(parent, SWT.BORDER | SWT.LEFT);
        loginText.setLayoutData(logPassGridData);

        Label passwordLabel = new Label(parent, SWT.BEGINNING);
        passwordLabel.setText(PASSWORD);
        passwordLabel.setLayoutData(labelGridData);

        passwordText = new Text(parent, SWT.BORDER | SWT.BEGINNING | SWT.PASSWORD);
        passwordText.setLayoutData(logPassGridData);

        GridData lineSeparatorGridData = new GridData(GridData.FILL_HORIZONTAL);
        lineSeparatorGridData.horizontalSpan = 3;

        Label separatorLabel = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
        separatorLabel.setLayoutData(lineSeparatorGridData);

        useProxyCheckbox = new Button(parent, SWT.CHECK);
        useProxyCheckbox.setText(USE_PROXY);
        useProxyCheckbox.setLayoutData(checkBoxGridData);

        proxySettingsLabel = new Label(parent, SWT.BEGINNING);
        proxySettingsLabel.setText(PROXY_SETTINGS);
        proxySettingsLabel.setLayoutData(checkBoxGridData);
        proxySettingsLabel.setFont(font);

        proxyHostLabel = new Label(parent, SWT.BEGINNING);
        proxyHostLabel.setText(PROXY_HOST);
        proxyHostLabel.setLayoutData(labelGridData);

        proxyHostText = new Text(parent, SWT.BORDER | SWT.LEFT);
        proxyHostText.setLayoutData(addressTextGridData);

        proxyPortLabel = new Label(parent, SWT.BEGINNING);
        proxyPortLabel.setText(PROXY_PORT);
        proxyPortLabel.setLayoutData(labelGridData);

        GridData portGridData = new GridData(GridData.BEGINNING, GridData.CENTER, false, false);
        portGridData.widthHint = 40;
        portGridData.horizontalSpan = 2;

        proxyPortText = new Text(parent, SWT.BORDER | SWT.LEFT);
        proxyPortText.setLayoutData(portGridData);

        proxyUserLabel = new Label(parent, SWT.BEGINNING);
        proxyUserLabel.setText(PROXY_USER);
        proxyUserLabel.setLayoutData(labelGridData);

        proxyUserText = new Text(parent, SWT.BORDER | SWT.LEFT);
        proxyUserText.setLayoutData(logPassGridData);

        proxyPasswordLabel = new Label(parent, SWT.BEGINNING);
        proxyPasswordLabel.setText(PROXY_PASSWORD);
        proxyPasswordLabel.setLayoutData(labelGridData);

        proxyPasswordText = new Text(parent, SWT.BORDER | SWT.LEFT | SWT.PASSWORD);
        proxyPasswordText.setLayoutData(logPassGridData);
        initButtons(parent);
        checkSettings();
    }

    private void initButtons(Composite parent) {
        GridLayout inputDataGridLayout = new GridLayout(1, false);

        Composite inputDataComposite = new Composite(parent, SWT.END);
        inputDataComposite.setLayout(inputDataGridLayout);

        GridData compositeData = new GridData(GridData.END, GridData.END, true, true);
        compositeData.horizontalSpan = 3;
        inputDataComposite.setLayoutData(compositeData);

        GridData gridDataSave = new GridData();
        gridDataSave.widthHint = 80;
        gridDataSave.heightHint = 30;

        saveButton = new Button(inputDataComposite, SWT.PUSH);
        saveButton.setText(SAVE_SETTINGS);
        saveButton.setLayoutData(gridDataSave);
        saveButton.setEnabled(false);
    }

    private void initListeners() {
        TextModifyListener textModifyListener = new TextModifyListener();

        loginText.addModifyListener(textModifyListener);
        passwordText.addModifyListener(textModifyListener);
        proxyHostText.addModifyListener(textModifyListener);
        proxyPortText.addModifyListener(textModifyListener);
        proxyUserText.addModifyListener(textModifyListener);
        proxyPasswordText.addModifyListener(textModifyListener);

        useProxyCheckbox.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                checkProxy();
                saveButton.setEnabled(true);
            }
        });

        saveButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                save();
                saveButton.setEnabled(false);
            }

        });
    }

    private void save() {
        String user = loginText.getText().trim();
        String password = passwordText.getText().trim();
        boolean proxy =useProxyCheckbox.getSelection();
        String proxyHost = proxyHostText.getText().trim();
        String proxyPort = proxyPortText.getText().trim();
        String proxyUser = proxyUserText.getText().trim();
        String proxyPassword = proxyPasswordText.getText().trim();
        try {
        	settingGetter.saveSettings(SETTINS_FILE, user, password,  proxyHost, proxyPort, proxyUser,
                    proxyPassword,proxy);
        } catch (IOException e) {
            MessageDialog.openError(shell, IO_EXCEPTION, IO_EXCEPTION);
            e.printStackTrace();
        }
    }

    private void checkSettings() {
        try {
        	settingGetter.loadSettings(SETTINS_FILE);
        } catch (IOException e) {
            MessageDialog.openError(shell, IO_EXCEPTION, IO_EXCEPTION);
            e.printStackTrace();
        } catch (ParseException e) {
            MessageDialog.openError(shell, PARSE_ERROR, PARSE_ERROR);
            e.printStackTrace();
        }
        
        loginText.setText(settingGetter.getLogin().trim());
        passwordText.setText(settingGetter.getPassword().trim());
        useProxyCheckbox.setSelection(settingGetter.isProxy());
        proxyHostText.setText(settingGetter.getProxyHost().trim());
        proxyPortText.setText(settingGetter.getProxyPort().trim());
        proxyUserText.setText(settingGetter.getProxyUser().trim());
        proxyPasswordText.setText(settingGetter.getProxyPassword().trim());
        checkProxy();
    }

    private void connect(String url, String user, String password) throws MalformedURLException, IOException {
        inputData = Connector.setConnection(url, user, password);
        dataManager.getInputStreamConnection(inputData);
    }

    private void checkProxy() {
        proxySettingsLabel.setEnabled(useProxyCheckbox.getSelection());
        proxyHostLabel.setEnabled(useProxyCheckbox.getSelection());
        proxyPortLabel.setEnabled(useProxyCheckbox.getSelection());
        proxyUserLabel.setEnabled(useProxyCheckbox.getSelection());
        proxyPasswordLabel.setEnabled(useProxyCheckbox.getSelection());
        proxyHostText.setEnabled(useProxyCheckbox.getSelection());
        proxyPortText.setEnabled(useProxyCheckbox.getSelection());
        proxyUserText.setEnabled(useProxyCheckbox.getSelection());
        proxyPasswordText.setEnabled(useProxyCheckbox.getSelection());
    }

    private class TextModifyListener implements ModifyListener {
        @Override
        public void modifyText(ModifyEvent e) {
            saveButton.setEnabled(true);
        }
    }

    @Override
    public void getSettings(String address) throws MalformedURLException, IOException {
       
        String user = loginText.getText().trim();
        String password = passwordText.getText().trim();
        connect(address, user, password);
    }

    private void signUpToTable() {
        settingGetter.registerObserver(this);
    }
}
