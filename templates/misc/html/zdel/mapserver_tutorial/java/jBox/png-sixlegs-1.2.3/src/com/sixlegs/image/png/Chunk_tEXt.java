// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

final class Chunk_tEXt
extends AbstractTextChunk
{
    Chunk_tEXt()
    {
        super(tEXt);
    }

    protected boolean isCompressed()
    {
        return false;
    }

    public String getTranslatedKeyword()
    {
        return null;
    }

    public String getLanguage()
    {
        return null;
    }
}
