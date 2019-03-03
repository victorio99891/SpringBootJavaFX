
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package pl.wiktor.springboot_javafx_example.utils;

import java.io.PrintWriter;
import java.io.Writer;


public class ExceptionWriter extends PrintWriter {
    public ExceptionWriter(Writer writer) {
        super(writer);
    }

    private String wrapAroundWithNewlines(String stringWithoutNewlines) {
        return ("\n" + stringWithoutNewlines + "\n");
    }


    public String getExceptionAsString(Throwable throwable) {
        throwable.printStackTrace(this);

        String exception = super.out.toString();

        return (wrapAroundWithNewlines(exception));
    }
}

