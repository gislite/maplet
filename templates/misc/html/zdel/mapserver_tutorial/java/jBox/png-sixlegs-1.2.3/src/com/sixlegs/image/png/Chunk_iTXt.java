// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;
import java.io.IOException;

final class Chunk_iTXt extends AbstractTextChunk implements TextChunk {
    private boolean compressed;
    private String language;
    private String translated;

    Chunk_iTXt()
    {
        super(iTXt);
    }

    public String getTranslatedKeyword()
    {
        return translated;
    }

    public String getLanguage()
    {
        return language;
    }

    protected boolean isCompressed()
    {
        return compressed;
    }

    protected String readValue()
    throws IOException
    {
        int flag = in_data.readByte();
        int method = in_data.readByte();
        if (flag == 1) {
            compressed = true;
            if (method != PngImage.COMPRESSION_TYPE_BASE) {
                throw new PngExceptionSoft("Unrecognized " + typeToString(type) +
                                           " compression method: " + method);
            }
        } else if (flag != 0) {
            throw new PngExceptionSoft("Illegal " + typeToString(type) +
                                       " compression flag: " + flag);
        }
        language = in_data.readString(PngImage.LATIN1_ENCODING);
        translated = in_data.readString(PngImage.UTF8_ENCODING);
        return super.readValue();
    }
}
