package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.utils.XMLReaderUtils;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.xml.sax.SAXException;

public class XMLParserExample  {

    public static void main(String[] args) throws IOException, TikaException, SAXException {
        //detecting the file type
        Metadata metadata = new Metadata();
        String fileName = "9201_100_77478_20230307122554.xml";
        FileInputStream inputstream = new FileInputStream(new File(fileName));
        ParseContext pcontext = new ParseContext();


        org.w3c.dom.Document document =  XMLReaderUtils.buildDOM(inputstream, pcontext);
        System.out.println(document.getElementsByTagName("RetailStoreID").item(0).getTextContent());


    }


}