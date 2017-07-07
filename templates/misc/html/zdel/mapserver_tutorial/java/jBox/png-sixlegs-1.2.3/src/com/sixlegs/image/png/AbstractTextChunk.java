// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.Hashtable;

abstract class AbstractTextChunk
extends KeyValueChunk
implements TextChunk
{
    static private Hashtable special_keys = new Hashtable();
    static {
        special_keys.put("Title", Boolean.TRUE);
        special_keys.put("Author", Boolean.TRUE);
        special_keys.put("Description", Boolean.TRUE);
        special_keys.put("Copyright", Boolean.TRUE);
        special_keys.put("Creation Time", Boolean.TRUE);
        special_keys.put("Software", Boolean.TRUE);
        special_keys.put("Disclaimer", Boolean.TRUE);
        special_keys.put("Warning", Boolean.TRUE);
        special_keys.put("Source", Boolean.TRUE);
        special_keys.put("Comment", Boolean.TRUE);
    }

    public String toString() { return getText(); }

    public String getKeyword() { return key; }
    public String getText() { return value; }
    abstract public String getTranslatedKeyword();
    abstract public String getLanguage();

    AbstractTextChunk(int type) { super(type); }

    protected String readKey()
    throws IOException
    {
        String key = super.readKey();
        if (special_keys.containsKey(key)) {
            String lowerkey = key.toLowerCase();
            Object replace = img.data.properties.get(lowerkey);
            if (replace == null || ((Chunk)replace).type != iTXt)
                img.data.properties.put(lowerkey, this);
        }
        img.data.textChunks.put(key, this);
        return key;
    }

    protected String readValue()
    throws IOException
    {
        return repairValue(super.readValue());
    }

    private static String repairValue(String val)
    {
        CharArrayWriter out_chars = new CharArrayWriter(val.length());
        try {
            char[] chs = val.toCharArray();
            int i = 0, p = 0;
            int L = chs.length;
            String endl = System.getProperty("line.separator");
            while (p < L) {
                char ch = chs[p++];
                switch (ch) {
                case '\r':
                    if (p < L && chs[p + 1] == '\n') break;
                case '\n':
                    out_chars.write(endl);
                    break;
                case '\t':
                    out_chars.write('\t');
                    break;
                default:
                    if (ch <= 31 || (ch >= 127 && ch <= 159)) {
                        out_chars.write('\\');
                        out_chars.write(Integer.toOctalString(ch));
                    } else {
                        out_chars.write(ch);
                    }
                }
            }
        } catch (IOException e) { }
        return out_chars.toString();
    }

    public String getChunkType()
    {
        return typeToString(type);
    }
}
