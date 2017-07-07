// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

final class Chunk_zTXt
extends AbstractTextChunk
{
    Chunk_zTXt()
    {
        super(zTXt);
    }

    protected boolean isCompressed()
    {
        return true;
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
