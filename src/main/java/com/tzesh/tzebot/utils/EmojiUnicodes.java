package com.tzesh.tzebot.utils;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.UnicodeEmoji;

public enum EmojiUnicodes {
    nowPlaying ("U+23EF"),
    stop ("U+23F9"),
    skip ("U+23ED"),
    loop ("U+1F501"),
    shuffle ("U+1F500"),
    next ("U+21AA"),
    previous ("U+21A9"),
    volumedown ("U+1F509"),
    volumeup ("U+1F50A"),
    queue ("U+1F4DC");

    private final String unicode;

        private EmojiUnicodes(String unicode) {
            this.unicode = unicode;
        }

        public UnicodeEmoji getUnicode() {
            return Emoji.fromUnicode(unicode);
        }

}
