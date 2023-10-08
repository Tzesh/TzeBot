package com.tzesh.tzebot.gui;

import com.tzesh.tzebot.core.config.ConfigurationManager;
import com.tzesh.tzebot.gui.utils.TextAreaOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.awt.event.*;
import java.io.PrintStream;

import static com.tzesh.tzebot.core.inventory.Inventory.PREFIXES;

/**
 * This is the GUI for TzeBot
 * @author tzesh
 */
public class TzeGUI extends javax.swing.JFrame {
    private javax.swing.JLabel labelApiKey;
    private javax.swing.JLabel labelBotToken;
    private javax.swing.JLabel labelConsole;
    private javax.swing.JLabel labelDiscordID;
    private javax.swing.JLabel labelTzeshIcon;
    private javax.swing.JLabel labelGithubLink;
    private javax.swing.JLabel labelPrefix;
    private javax.swing.JLabel labelProgressInfo;
    private javax.swing.JLabel labelProgress;
    private javax.swing.JLabel labelShard;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JLabel labelVersionNumber;
    private javax.swing.JTextField textFieldApiKey;
    private javax.swing.JTextField textFieldOwnerID;
    private javax.swing.JTextField textFieldPrefix;
    private javax.swing.JTextField textFieldToken;
    private javax.swing.JFormattedTextField formattedTextFieldShard;
    private javax.swing.JTextArea textAreaConsole;
    private javax.swing.JScrollPane scrollPaneConsole;
    private javax.swing.JButton buttonStart;
    private javax.swing.JButton buttonUpdate;
    private int shards = 1;
    private final Logger LOGGER;


    public TzeGUI() {
        ConfigurationManager.createENV();
        initComponents();
        initializeConsole();
        checkVariables();
        checkVersion();
        ConfigurationManager.loadInventory();
        addWindowListener();
        addButtonStartEventListener();
        this.LOGGER = LoggerFactory.getLogger(TzeGUI.class);
        LOGGER.info("GUI has been initialized");
    }

    private void initializeConsole() {
        TextAreaOutputStream taos = new TextAreaOutputStream(textAreaConsole, 60);
        PrintStream ps = new PrintStream(taos);
        System.setOut(ps);
        System.setErr(ps);
    }

    private void addWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ConfigurationManager.saveInventory();
                System.exit(0);
            }
        });
    }

    private void addButtonStartEventListener() {
        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (buttonStart == e.getSource()) {
                    buttonStart.setEnabled(false);
                    textFieldToken.setEnabled(false);
                    textFieldOwnerID.setEnabled(false);
                    textFieldPrefix.setEnabled(false);
                    textFieldApiKey.setEnabled(false);
                    formattedTextFieldShard.setEnabled(false);
                }
            }
        });
    }

    private void checkVariables() {
        if (ConfigurationManager.getEnvKey("token") == null || ConfigurationManager.getEnvKey("token").equals("")) {
            textFieldToken.setText("Not-installed");
        } else {
            textFieldToken.setText(ConfigurationManager.getEnvKey("token"));
        }
        if (ConfigurationManager.getEnvKey("Owner") == null || ConfigurationManager.getEnvKey("Owner").equals("")) {
            textFieldOwnerID.setText("Not-installed");
        } else {
            textFieldOwnerID.setText(ConfigurationManager.getEnvKey("Owner"));
        }
        if (ConfigurationManager.getEnvKey("Pre") == null || ConfigurationManager.getEnvKey("pre").equals("")) {
            textFieldPrefix.setText(".");
        } else {
            textFieldPrefix.setText(ConfigurationManager.getEnvKey("Pre"));
        }
        if (ConfigurationManager.getEnvKey("Key") == null || ConfigurationManager.getEnvKey("Key").equals("")) {
            textFieldApiKey.setText("Not-installed");
        } else {
            textFieldApiKey.setText(ConfigurationManager.getEnvKey("Key"));
        }
        if (ConfigurationManager.getEnvKey("shard") == null || ConfigurationManager.getEnvKey("shard").equals("")) {
            formattedTextFieldShard.setText("1");
        } else {
            formattedTextFieldShard.setText(Integer.toString((PREFIXES.size() / 1500) + 1));
        }

        formattedTextFieldShard.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9')
                        || (c == KeyEvent.VK_BACK_SPACE)
                        || (c == KeyEvent.VK_DELETE))) {
                    getToolkit().beep();
                    e.consume();
                }
            }
        });

        textFieldOwnerID.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9')
                        || (c == KeyEvent.VK_BACK_SPACE)
                        || (c == KeyEvent.VK_DELETE))) {
                    getToolkit().beep();
                    e.consume();
                }
            }
        });
        labelProgressInfo.setText("All .env variables have been checked.");
    }

    private void checkVersion() {
        labelVersionNumber.setText("v" + ConfigurationManager.getCurrentVersion());
        if (ConfigurationManager.isLatestVersion()) {
            LOGGER.info("You are using the latest version of TzeBot v" + ConfigurationManager.getCurrentVersion());
        } else {
            buttonUpdate.setVisible(true);
        }
    }

    private void getVariables() {
        shards = Integer.parseInt(formattedTextFieldShard.getText());
        if (shards <= 1) {
            shards = 1;
        }
        ConfigurationManager.saveEnv(textFieldToken.getText(), textFieldPrefix.getText(), textFieldOwnerID.getText(), textFieldApiKey.getText(), formattedTextFieldShard.getText());
        labelProgressInfo.setText("All .env variables have been saved.");
    }

    private void initComponents() {
        buttonStart = new javax.swing.JButton();
        labelTitle = new javax.swing.JLabel();
        labelDiscordID = new javax.swing.JLabel();
        labelPrefix = new javax.swing.JLabel();
        labelBotToken = new javax.swing.JLabel();
        labelApiKey = new javax.swing.JLabel();
        textFieldOwnerID = new javax.swing.JTextField();
        textFieldPrefix = new javax.swing.JTextField();
        textFieldToken = new javax.swing.JTextField();
        textFieldApiKey = new javax.swing.JTextField();
        scrollPaneConsole = new javax.swing.JScrollPane();
        textAreaConsole = new javax.swing.JTextArea();
        labelProgress = new javax.swing.JLabel();
        labelProgressInfo = new javax.swing.JLabel();
        labelConsole = new javax.swing.JLabel();
        labelGithubLink = new javax.swing.JLabel();
        labelShard = new javax.swing.JLabel();
        formattedTextFieldShard = new javax.swing.JFormattedTextField();
        labelTzeshIcon = new javax.swing.JLabel();
        buttonUpdate = new javax.swing.JButton();
        labelVersionNumber = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TzeBot - Advanced Discord Bot & Substructure");
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/tzesh-icon.png")).getImage());
        setMaximizedBounds(new java.awt.Rectangle(0, 0, 1000, 500));
        setMaximumSize(new java.awt.Dimension(1000, 500));
        setMinimumSize(new java.awt.Dimension(1000, 500));
        setName("mainWindow"); // NOI18N
        setResizable(false);

        buttonStart.setText("Start");
        buttonStart.setToolTipText("You can start the bot after making sure that all variables are correct");
        buttonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        labelTitle.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        labelTitle.setText("TzeBot - Advanced Discord Bot & Substructure");
        labelTitle.setToolTipText("");

        labelDiscordID.setText("Owner Discord ID:");

        labelPrefix.setText("Default Prefix: ");

        labelBotToken.setText("Discord Bot Token:");

        labelApiKey.setText("Youtube API Key:");

        textFieldOwnerID.setText("Discord ID");
        textFieldOwnerID.setMaximumSize(new java.awt.Dimension(420, 30));
        textFieldOwnerID.setMinimumSize(new java.awt.Dimension(420, 30));
        textFieldOwnerID.setPreferredSize(new java.awt.Dimension(420, 30));

        textFieldPrefix.setText(".");
        textFieldPrefix.setMaximumSize(new java.awt.Dimension(16, 30));
        textFieldPrefix.setMinimumSize(new java.awt.Dimension(16, 30));
        textFieldPrefix.setPreferredSize(new java.awt.Dimension(16, 30));

        textFieldToken.setText("Bot_TOKEN");
        textFieldToken.setMaximumSize(new java.awt.Dimension(420, 30));
        textFieldToken.setMinimumSize(new java.awt.Dimension(420, 30));
        textFieldToken.setPreferredSize(new java.awt.Dimension(420, 30));

        textFieldApiKey.setText("API_KEY");
        textFieldApiKey.setMaximumSize(new java.awt.Dimension(420, 30));
        textFieldApiKey.setMinimumSize(new java.awt.Dimension(420, 30));
        textFieldApiKey.setPreferredSize(new java.awt.Dimension(420, 30));

        textAreaConsole.setEditable(false);
        textAreaConsole.setColumns(30);
        textAreaConsole.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        textAreaConsole.setRows(30);
        textAreaConsole.setTabSize(5);
        scrollPaneConsole.setViewportView(textAreaConsole);

        labelProgress.setText("Progress:");

        labelProgressInfo.setText("Waiting to be started");

        labelConsole.setText("Console:");

        labelGithubLink.setText("github.com/Tzesh/TzeBot");

        labelShard.setText("Shard(s):");

        formattedTextFieldShard.setText("Shard");
        formattedTextFieldShard.setMaximumSize(new java.awt.Dimension(30, 30));
        formattedTextFieldShard.setMinimumSize(new java.awt.Dimension(30, 30));
        formattedTextFieldShard.setPreferredSize(new java.awt.Dimension(30, 30));

        labelTzeshIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tzesh-icon.png")));

        buttonUpdate.setText("Update");
        buttonUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });
        buttonUpdate.setVisible(false);

        labelVersionNumber.setText("v" + ConfigurationManager.getCurrentVersion());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(15, 15, 15)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(labelDiscordID)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(labelConsole))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(textFieldToken, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(textFieldOwnerID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(labelPrefix)
                                                                        .addComponent(labelTitle)
                                                                        .addComponent(textFieldPrefix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(labelBotToken)
                                                                        .addComponent(labelApiKey)
                                                                        .addComponent(textFieldApiKey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(labelShard)
                                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(formattedTextFieldShard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                .addComponent(buttonStart)
                                                                                                .addGap(18, 18, 18)
                                                                                                .addComponent(buttonUpdate))
                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                .addComponent(labelProgress)
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(labelProgressInfo)))
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)))
                                                                .addComponent(labelTzeshIcon)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(labelGithubLink)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(labelVersionNumber)
                                                .addGap(65, 65, 65)))
                                .addComponent(scrollPaneConsole, javax.swing.GroupLayout.PREFERRED_SIZE, 698, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(labelConsole)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(13, 13, 13)
                                                                .addComponent(labelDiscordID)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(textFieldOwnerID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(labelPrefix)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(textFieldPrefix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(labelBotToken)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(textFieldToken, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(labelApiKey)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(textFieldApiKey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(labelShard)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(formattedTextFieldShard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(buttonStart)
                                                                        .addComponent(buttonUpdate))
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(labelTzeshIcon)
                                                                        .addComponent(labelGithubLink))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(labelVersionNumber)
                                                                .addGap(11, 11, 11))))
                                        .addComponent(scrollPaneConsole, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(labelTitle)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelProgress)
                                        .addComponent(labelProgressInfo))
                                .addGap(39, 39, 39))
        );

        getAccessibleContext().setAccessibleName("MainWindow");
        getAccessibleContext().setAccessibleDescription("GUI for controlling TzeBot");

        pack();
    }

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {
        getVariables();
        String apiKey = textFieldApiKey.getText();
        String botToken = textFieldToken.getText();
        labelProgressInfo.setText("Bot started.");
        try {
            ConfigurationManager.startBot(apiKey, botToken, shards);
        } catch (LoginException e) {
            LOGGER.error("An error occurred while bot was trying to start: " +  e.getMessage());
        }

    }

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(ConfigurationManager.getDownloadURL()));
        } catch (Exception e) {
            LOGGER.error("An error occurred during opening the update page: " +  e.getMessage());
        }
    }

    public void start() {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException ex) {
            LOGGER.error(ex.toString());
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TzeGUI tzegui = new TzeGUI();
                tzegui.setSize(1500, 550);
                tzegui.setVisible(true);
            }
        });
    }
}
