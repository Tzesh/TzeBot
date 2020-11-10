package TzeBot.gui;

import TzeBot.essentials.Config;
import TzeBot.essentials.LanguageManager;
import TzeBot.essentials.Listener;
import net.dv8tion.jda.api.GatewayEncoding;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.sql.SQLException;

import static TzeBot.essentials.Config.PREFIXES;

public class TzeGUI extends javax.swing.JFrame {

    int shards = 1;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField apiKEY;
    private javax.swing.JLabel apiKeyText;
    private javax.swing.JLabel botTokenText;
    private javax.swing.JTextArea console;
    private javax.swing.JLabel consoleText;
    private javax.swing.JLabel discordIDText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField ownerID;
    private javax.swing.JTextField prefix;
    private javax.swing.JLabel prefixText;
    private javax.swing.JLabel progressInfo;
    private javax.swing.JLabel progressText;
    private javax.swing.JFormattedTextField shard;
    private javax.swing.JLabel shardText;
    private javax.swing.JButton startButton;
    private javax.swing.JLabel titleText;
    private javax.swing.JTextField token;
    private javax.swing.JButton updateButton;
    private javax.swing.JLabel versionNumber;
    public TzeGUI() {
        Config.createENV();
        Config.getDatabase();
        initComponents();
        updateButton.setVisible(false);
        TextAreaOutputStream taos = new TextAreaOutputStream(console, 60);
        PrintStream ps = new PrintStream(taos);
        System.setOut(ps);
        System.setErr(ps);
        checkVariables();
        checkVersion();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Config.saveDatabase();
                System.exit(0);
            }
        });
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (startButton == e.getSource()) {
                    startButton.setEnabled(false);
                    token.setEnabled(false);
                    ownerID.setEnabled(false);
                    prefix.setEnabled(false);
                    apiKEY.setEnabled(false);
                    shard.setEnabled(false);
                }
            }
        });
        System.out.println("GUI has been initialized.");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws LoginException, SQLException {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
    }

    public final void checkVariables() {
        if (Config.get("token") == null || Config.get("token").equals("")) {
            token.setText("Not-installed");
        } else {
            token.setText(Config.get("token"));
        }
        if (Config.get("Owner") == null || Config.get("Owner").equals("")) {
            ownerID.setText("Not-installed");
        } else {
            ownerID.setText(Config.get("Owner"));
        }
        if (Config.get("Pre") == null || Config.get("pre").equals("")) {
            prefix.setText(".");
        } else {
            prefix.setText(Config.get("Pre"));
        }
        if (Config.get("Key") == null || Config.get("Key").equals("")) {
            apiKEY.setText("Not-installed");
        } else {
            apiKEY.setText(Config.get("Key"));
        }
        if (Config.get("shard") == null || Config.get("shard").equals("")) {
            shard.setText("1");
        } else {
            shard.setText(Integer.toString((PREFIXES.size() / 1500) + 1));
        }

        shard.addKeyListener(new KeyAdapter() {
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

        ownerID.addKeyListener(new KeyAdapter() {
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
        progressInfo.setText("All .env variables have been checked.");
    }

    public void checkVersion() {
        versionNumber.setText("v" + Config.currentVersion);
        if (Config.versionControl() == true) {
            System.out.println("You are using the latest version of TzeBot v" + Config.currentVersion);
        } else {
            updateButton.setVisible(true);
        }
    }

    public void getvariables() {
        shards = Integer.parseInt(shard.getText());
        if (shards <= 1) {
            shards = 1;
        }
        Config.save(token.getText(), prefix.getText(), ownerID.getText(), apiKEY.getText(), shard.getText());
        progressInfo.setText("All .env variables have been saved.");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        startButton = new javax.swing.JButton();
        titleText = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        discordIDText = new javax.swing.JLabel();
        prefixText = new javax.swing.JLabel();
        botTokenText = new javax.swing.JLabel();
        apiKeyText = new javax.swing.JLabel();
        ownerID = new javax.swing.JTextField();
        prefix = new javax.swing.JTextField();
        token = new javax.swing.JTextField();
        apiKEY = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        console = new javax.swing.JTextArea();
        progressText = new javax.swing.JLabel();
        progressInfo = new javax.swing.JLabel();
        consoleText = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        shardText = new javax.swing.JLabel();
        shard = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        updateButton = new javax.swing.JButton();
        versionNumber = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TzeBot - Discord Bot for Public Use");
        setMaximizedBounds(new java.awt.Rectangle(0, 0, 1000, 500));
        setMaximumSize(new java.awt.Dimension(1000, 500));
        setMinimumSize(new java.awt.Dimension(1000, 500));
        setName("mainWindow"); // NOI18N
        setResizable(false);

        startButton.setText("Start");
        startButton.setToolTipText("You can start TzeBot after you've managed to set all necessary settings.");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        titleText.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        titleText.setText("TzeBot - Discord Bot for Public Use");
        titleText.setToolTipText("");

        discordIDText.setText("Discord ID of Owner:");

        prefixText.setText("Default Prefix: ");

        botTokenText.setText("Discord Bot Token:");

        apiKeyText.setText("Youtube API Key:");

        ownerID.setText("Discord ID");
        ownerID.setMaximumSize(new java.awt.Dimension(420, 30));
        ownerID.setMinimumSize(new java.awt.Dimension(420, 30));
        ownerID.setPreferredSize(new java.awt.Dimension(420, 30));

        prefix.setText(".");
        prefix.setMaximumSize(new java.awt.Dimension(16, 30));
        prefix.setMinimumSize(new java.awt.Dimension(16, 30));
        prefix.setPreferredSize(new java.awt.Dimension(16, 30));

        token.setText("Bot_TOKEN");
        token.setMaximumSize(new java.awt.Dimension(420, 30));
        token.setMinimumSize(new java.awt.Dimension(420, 30));
        token.setPreferredSize(new java.awt.Dimension(420, 30));

        apiKEY.setText("API_KEY");
        apiKEY.setMaximumSize(new java.awt.Dimension(420, 30));
        apiKEY.setMinimumSize(new java.awt.Dimension(420, 30));
        apiKEY.setPreferredSize(new java.awt.Dimension(420, 30));

        console.setEditable(false);
        console.setColumns(30);
        console.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        console.setRows(30);
        console.setTabSize(5);
        jScrollPane1.setViewportView(console);

        progressText.setText("Progress:");

        progressInfo.setText("Waiting to be started.");

        consoleText.setText("Console:");

        jLabel9.setText("github.com/Tzesh/TzeBot");

        shardText.setText("Shard(s):");

        shard.setText("Shard");
        shard.setMaximumSize(new java.awt.Dimension(30, 30));
        shard.setMinimumSize(new java.awt.Dimension(30, 30));
        shard.setPreferredSize(new java.awt.Dimension(30, 30));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Tzesh Amblem - Black -non - Kopya.png"))); // NOI18N

        updateButton.setText("Update");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        versionNumber.setText("v1.5.1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(15, 15, 15)
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(discordIDText)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(consoleText))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(token, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(ownerID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(prefixText)
                                                                        .addComponent(titleText)
                                                                        .addComponent(prefix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(botTokenText)
                                                                        .addComponent(apiKeyText)
                                                                        .addComponent(apiKEY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(shardText)
                                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(shard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                .addComponent(startButton)
                                                                                                .addGap(18, 18, 18)
                                                                                                .addComponent(updateButton))
                                                                                        .addGroup(layout.createSequentialGroup()
                                                                                                .addComponent(progressText)
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(progressInfo)))
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)))
                                                                .addComponent(jLabel10)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel9)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(versionNumber)
                                                .addGap(65, 65, 65)))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 698, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(consoleText)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(13, 13, 13)
                                                                .addComponent(discordIDText)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(ownerID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(prefixText)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(prefix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(botTokenText)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(token, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(apiKeyText)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(apiKEY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(shardText)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(shard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(startButton)
                                                                        .addComponent(updateButton))
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(jLabel10)
                                                                        .addComponent(jLabel9))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(versionNumber)
                                                                .addGap(11, 11, 11))))
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(titleText)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(progressText)
                                        .addComponent(progressInfo))
                                .addGap(39, 39, 39))
        );

        getAccessibleContext().setAccessibleName("MainWindow");
        getAccessibleContext().setAccessibleDescription("GUI for controlling TzeBot");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        getvariables();
        Config.saveForAQuarter();
        LanguageManager.getMessages();
        progressInfo.setText("Bot started.");
        try {
            DefaultShardManagerBuilder.createDefault(apiKEY.getText())
                    .setToken(token.getText())
                    .addEventListeners(new Listener())
                    .setShardsTotal(shards)
                    .setActivity(Activity.listening(".help"))
                    .disableCache(CacheFlag.ACTIVITY, CacheFlag.MEMBER_OVERRIDES)
                    .setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER))
                    .setChunkingFilter(ChunkingFilter.NONE)
                    .disableIntents(GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.GUILD_INVITES)
                    .setStatus(OnlineStatus.DO_NOT_DISTURB)
                    .setGatewayEncoding(GatewayEncoding.ETF)
                    .setAutoReconnect(true)
                    .setLargeThreshold(50)
                    .build();
        } catch (LoginException exception) {
            System.out.println("An error occured please make sure you have set all variables properly.");
        }
    }//GEN-LAST:event_startButtonActionPerformed

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create("https://github.com/Tzesh/TzeBot/releases"));
        } catch (Exception e) {
            System.out.println("An error occured during opening the update page.");
        }
    }//GEN-LAST:event_updateButtonActionPerformed

    public void appendText(String text) {
        console.append(text);
    }

    public void start() throws LoginException, SQLException {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TzeGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TzeGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TzeGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TzeGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TzeGUI tzegui = new TzeGUI();
                tzegui.size().height = 500;
                tzegui.size().width = 1000;
                tzegui.setVisible(true);
            }
        });
    }
    // End of variables declaration//GEN-END:variables
}
