package com.lux.generator.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.json.simple.parser.ParseException;

import com.lux.generator.events.ConnectionEvent;
import com.lux.generator.listeners.DataPanelListener;
import com.lux.generator.manager.DataManager;
import com.lux.generator.settings.manager.SettingGetter;


public class ResultPanel implements DataPanelListener {

    private static final String ALL_DATA_WILL_BE_DELETED = "All data will be deleted. Are you sure?";
    // Error messages
    private static final String IOERROR = "I/O Error";
    private static final String MESSAGE_FILE_WRITE_ERROR = "Error occured while file was writing";
    private static final String PARSE_ERROR = "Parsing error";
    private static final String CONNECTION_ERROR = "Connection Error";
    private static final String INPUT_FAILED = "Input failed, check your network settings";
    private static final String PAGE_NOT_FOUND = "Page not found";
    private static final String WRONG_LOGIN_OR_PASSWORD = "Wrong login or password";
    // Button names
    private static final String GET_COMMAND = "Get command";
    private static final String CLEAN = "Clean";
    private static final String SAVE = "Save to file";
    private static final String GET_DATA = "Get numbers";
    private static final String CREATE_EXEC = "Create Exec";
    private static final String DELETE = "Delete item";
    // Label names
    private static final String ADDRESS="Address:";
    private static final String COMMAND = "Command:";
    private static final String RESULT = "Result:";
    private static final String[] TABLE_HEADER = { "name", "searched by", "priority", "command", "project name",
            " number ", "suffix" };

    private static final int COMMAND_COLUMN_WIDTH = 200;

    private InputStream inputData;
    private Shell shell;
    private Table table;
    private Button cleanButton, saveButton, getDataButton, createExecButton,  deleteButton,
            commandButton;
    private TableViewer tableViever;
    private SettingGetter settingGetter;
    private DataManager dataManager;
    private Text addressText, areaText;

    private Object selections[];
    private int connectionCode;

    public ResultPanel(Composite parent, SettingGetter settingGetter, DataManager dataManager) {
        shell = parent.getShell();
        this.settingGetter = settingGetter;
        this.dataManager = dataManager;
        parent.setLayout(new GridLayout(1, true));
        initUI(parent);
        initFocusListener();
        signUpToTable();
    }

    private void initUI(Composite parent) {
        SashForm sashForm = new SashForm(parent, SWT.VERTICAL | SWT.SMOOTH);
        GridData sashGridData = new GridData(GridData.FILL_BOTH);
        sashForm.setLayoutData(sashGridData);

        Composite commandComposite = new Composite(sashForm, SWT.FILL);
        GridLayout textGridLayout = new GridLayout(1, true);
        commandComposite.setLayout(textGridLayout);
        
        Label addressLabel = new Label(commandComposite, SWT.BEGINNING);
        FontData fontData = addressLabel.getFont().getFontData()[0];
        Font font = new Font(null, new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));
        addressLabel.setText(ADDRESS);
        addressLabel.setFont(font);
        
        addressText=new Text(commandComposite, SWT.BORDER);
        addressText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
          
        Label commandLabel = new Label(commandComposite, SWT.BEGINNING);
        commandLabel.setText(COMMAND);
        commandLabel.setFont(font);

        areaText = new Text(commandComposite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        areaText.setLayoutData(sashGridData);

        Composite tableComposite = new Composite(sashForm, SWT.FILL);
        tableComposite.setLayout(textGridLayout);

        Label resultTableLable = new Label(tableComposite, SWT.BEGINNING);
        resultTableLable.setText(RESULT);
        resultTableLable.setFont(font);

        tableViever = new TableViewer(tableComposite,
                SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        tableViever.setContentProvider(ArrayContentProvider.getInstance());


        table = tableViever.getTable();
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        sashForm.setWeights(new int[] { 1, 2 });
        createMultilineTableItems();
        makeEditable();
        initButtons(parent);
    }


    private void initButtons(Composite parent) {
        GridLayout inputDataGridLayout = new GridLayout(8, true);
        Composite inputDataComposite = new Composite(parent, SWT.END);
        inputDataComposite.setLayout(inputDataGridLayout);
        GridData compositeData = new GridData();
        compositeData.horizontalAlignment = GridData.END;
        inputDataComposite.setLayoutData(compositeData);
        GridData buttinGridData = new GridData();
        buttinGridData.widthHint = 80;
        buttinGridData.heightHint = 30;

        commandButton = new Button(inputDataComposite, SWT.PUSH);
        commandButton.setText(GET_COMMAND);
        commandButton.setLayoutData(buttinGridData);
        commandButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                getCommand();
            }
        });

        getDataButton = new Button(inputDataComposite, SWT.PUSH);
        getDataButton.setText(GET_DATA);
        getDataButton.setLayoutData(buttinGridData);
        getDataButton.setEnabled(false);
        getDataButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                getNumbers();
            }
        });

        createExecButton = new Button(inputDataComposite, SWT.PUSH);
        createExecButton.setText(CREATE_EXEC);
        createExecButton.setLayoutData(buttinGridData);
        createExecButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                createFile();
            }
        });

        deleteButton = new Button(inputDataComposite, SWT.PUSH);
        deleteButton.setText(DELETE);
        deleteButton.setEnabled(false);
        deleteButton.setLayoutData(buttinGridData);
        deleteButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                delete();
            }
        });

        cleanButton = new Button(inputDataComposite, SWT.PUSH);
        cleanButton.setText(CLEAN);
        cleanButton.setLayoutData(buttinGridData);
        cleanButton.setEnabled(false);
        cleanButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                clearTable();
            }
        });
        resizeColumns();
    }

    private void getNumbers() {
        try {
            settingGetter.connection(addressText.getText().trim());
            dataManager.addNumbers(inputData);
        } catch (MalformedURLException e1) {
            MessageDialog.openError(shell, IOERROR, IOERROR);
            e1.printStackTrace();
        } catch (IOException e1) {
            getErrorMessage();
            e1.printStackTrace();
        } catch (ParseException e) {
            MessageDialog.openError(shell, PARSE_ERROR, PARSE_ERROR);
            e.printStackTrace();
        } finally {
            try {
                if (inputData != null) {
                    inputData.close();
                }
            } catch (IOException e) {
                MessageDialog.openError(shell, IOERROR, IOERROR);
                e.printStackTrace();
            }
        }
    }

    private void getCommand() {
        dataManager.createWay(areaText.getText().trim());
    }

    private void getErrorMessage() {
        switch (connectionCode) {
        case 401:
            MessageDialog.openError(shell, CONNECTION_ERROR, WRONG_LOGIN_OR_PASSWORD);
            break;
        case 404:
            MessageDialog.openError(shell, CONNECTION_ERROR, PAGE_NOT_FOUND);
            break;
        default:
            MessageDialog.openError(shell, CONNECTION_ERROR, INPUT_FAILED);
            break;
        }
    }

    @Override
    public void getInputData(ConnectionEvent connectionEvent) {
        inputData = connectionEvent.getIs();
    }



    private void delete() {
        deleteButton.setEnabled(false);
        dataManager.deleteItem(selections);
    }

   

    private File createFileDialog(String action, String[] filter, int swtType) {
        FileDialog fd = new FileDialog(shell, swtType);
        fd.setText(action);
        fd.setFilterExtensions(filter);
        File file = null;
        if (fd.open() != null) {
            file = new File(fd.getFilterPath() + "\\" + fd.getFileName());
        }
        return file;
    }

    private void createFile() {
        File file = createFileDialog(SAVE, new String[] { "*.bat", "*.sh" }, SWT.SAVE);
        try {
            if (file != null) {
                dataManager.saveBatFile(file, areaText.getText());
            }
        } catch (Exception e) {
            MessageDialog.openError(shell, IOERROR, MESSAGE_FILE_WRITE_ERROR);
        }
    }

    private void initFocusListener() {
        table.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent arg0) {
                selections = ((IStructuredSelection) tableViever.getSelection()).toArray();
                if (selections != null) {
                    deleteButton.setEnabled(true);
                } else {
                    deleteButton.setEnabled(false);
                }
            }
        });
    }

    private void signUpToTable() {
        dataManager.registerObserver(this);
    }

    public void setDataToTable() {
        tableViever.setInput(dataManager.getData());
        tableViever.refresh();
        setRowColor();
        createMultilineTableItems();
        if (tableViever.getTable().getItems().length > 0) {
            cleanButton.setEnabled(true);
            saveButton.setEnabled(true);
            getDataButton.setEnabled(true);
        }
    }

    public void setDataToTable(String command,String urlAddress) {
        tableViever.setInput(dataManager.getData());
        areaText.setText(command);
        addressText.setText(urlAddress);
        tableViever.refresh();
        setRowColor();
        resizeColumns();
        createMultilineTableItems();
        if (tableViever.getTable().getItems().length > 0) {
            cleanButton.setEnabled(true);
            saveButton.setEnabled(true);
            getDataButton.setEnabled(true);
        }
    }

    private void clearTable() {
        if (table.getItems().length > 0 && confirmClear()) {
            dataManager.clean();
            cleanButton.setEnabled(false);
            saveButton.setEnabled(false);
            getDataButton.setEnabled(false);
        }
    }

    private boolean confirmClear() {
        MessageBox dialog = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
        dialog.setText(CLEAN);
        dialog.setMessage(ALL_DATA_WILL_BE_DELETED);
        int buttonID = dialog.open();
        return buttonID == SWT.OK;
    }

    private void setRowColor() {
        Color yellow = shell.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND);
        Color white = shell.getDisplay().getSystemColor(SWT.COLOR_WHITE);
        TableItem[] ti = table.getItems();
        for (int i = 0; i < ti.length; i++) {
            if (i % 2 == 0) {
                ti[i].setBackground(yellow);
            } else {
                ti[i].setBackground(white);
            }
        }
    }

    private void resizeColumns() {
        for (int i = 0; i < TABLE_HEADER.length; i++) {
            if (i != 3) {
                table.getColumn(i).pack();
            } else {
                table.getColumn(i).setWidth(COMMAND_COLUMN_WIDTH);
            }
        }
    }

    private void updateData(TableItem localRow) {
        Object selection = ((IStructuredSelection) tableViever.getSelection()).getFirstElement();
        if (selection != null) {
            dataManager.updateItem(selection, localRow.getText(0), localRow.getText(1), localRow.getText(4),
                    localRow.getText(6), localRow.getText(2), localRow.getText(5), localRow.getText(3));
        }
    }

    private void createMultilineTableItems() {
        Listener paintListener = new Listener() {
            public void handleEvent(Event event) {
                switch (event.type) {
                case SWT.MeasureItem: {
                    TableItem item = (TableItem) event.item;
                    String text = item.getText(event.index);
                    Point size = event.gc.textExtent(text);
                    event.height = Math.max(event.height, size.y);
                    break;
                }
                case SWT.PaintItem: {
                    TableItem item = (TableItem) event.item;
                    String text = item.getText(event.index);
                    event.gc.drawText(text, event.x, event.y, true);
                    break;
                }
                case SWT.EraseItem: {
                    event.detail &= ~SWT.FOREGROUND;
                    break;
                }
                }
            }
        };
        table.addListener(SWT.MeasureItem, paintListener);
        table.addListener(SWT.PaintItem, paintListener);
        table.addListener(SWT.EraseItem, paintListener);
    }

    private void makeEditable() {
        final TableCursor cursor = new TableCursor(table, SWT.MULTI);
        final ControlEditor editor = new ControlEditor(cursor);
        editor.grabHorizontal = true;
        editor.grabVertical = true;
        cursor.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                table.setSelection(new TableItem[] { cursor.getRow() });
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                editCell(cursor, editor);
            }
        });
        cursor.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent e) {
                editCell(cursor, editor);
            }
        });
        multiSelection(cursor);
    }

    private void editCell(final TableCursor cursor, final ControlEditor editor) {
        table.setSelection(new TableItem[] { cursor.getRow() });
        final Text text = new Text(cursor, SWT.MULTI);
        TableItem row = cursor.getRow();
        int column = cursor.getColumn();
        text.setText(row.getText(column).trim());
        text.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent event) {
                if (event.character == SWT.CR & (event.stateMask & SWT.SHIFT) == 0) {
                    row.setText(column, text.getText().trim());
                    updateData(row);
                    text.dispose();
                }
                if (event.character == SWT.ESC) {
                    text.dispose();
                }
            }
        });
        Listener textListener = getEditCellTextListener(text, row, column);
        text.addListener(SWT.FocusOut, textListener);
        text.addListener(SWT.Traverse, textListener);
        editor.setEditor(text);
        text.setFocus();
        text.selectAll();
    }

    private Listener getEditCellTextListener(final Text text, TableItem row, int column) {
        Listener textListener = new Listener() {
            public void handleEvent(final Event e) {
                switch (e.type) {
                case SWT.FocusOut:
                    row.setText(column, text.getText().trim());
                    updateData(row);
                    text.dispose();
                    break;
                case SWT.Traverse:
                    switch (e.detail) {
                    case SWT.TRAVERSE_ESCAPE:
                        e.doit = false;
                    }
                    break;
                }
            }
        };
        return textListener;
    }

    private void multiSelection(final TableCursor cursor) {
        cursor.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.keyCode == SWT.CTRL || e.keyCode == SWT.SHIFT || (e.stateMask & SWT.CONTROL) != 0
                        || (e.stateMask & SWT.SHIFT) != 0) {
                    cursor.setVisible(false);
                }
            }
        });
        table.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.keyCode == SWT.CONTROL && (e.stateMask & SWT.SHIFT) != 0)
                    return;
                if (e.keyCode == SWT.SHIFT && (e.stateMask & SWT.CONTROL) != 0)
                    return;
                if (e.keyCode != SWT.CONTROL && (e.stateMask & SWT.CONTROL) != 0)
                    return;
                if (e.keyCode != SWT.SHIFT && (e.stateMask & SWT.SHIFT) != 0)
                    return;
                TableItem[] selection = table.getSelection();
                TableItem row = (selection.length == 0) ? table.getItem(table.getTopIndex()) : selection[0];
                table.showItem(row);
                cursor.setSelection(row, 0);
                cursor.setVisible(true);
                cursor.setFocus();
            }
        });
    }
}