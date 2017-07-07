// Copyright (C) 1998, 1999, 2001 Chris Nokleberg
// Please see included LICENSE.TXT

package com.sixlegs.image.png;

/**
 * Common interface to all PNG text chunks (tEXt, zTXt, iTXt).
 * @see PngImage#getTextChunk
 * @see PngImage#getProperty
 */
public interface TextChunk
{
    /** 
     * Returns the Latin-1 [ISO-8859-1] encoded keyword
     * of this TextChunk.
     */
    String getKeyword();

    /** 
     * Returns a translation of the keyword into the language
     * used by this TextChunk, or null if unspecified.
     */
    String getTranslatedKeyword();

    /** 
     * Returns the language [RFC-1766] used by the translated 
     * keyword and the text, or null if unspecified.
     */
    String getLanguage();

    /**
     * Returns the text of this TextChunk.
     */
    String getText();

    /**
     * Returns the original chunk type of this TextChunk: "tEXt",
     * "zTXt", or "iTXt".
     */
    String getChunkType();
}
