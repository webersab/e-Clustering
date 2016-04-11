package de.dkt.eservices.emallet.modules;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Input2CharSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.PrintInputAndTarget;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceLowercase;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.pipe.iterator.FileIterator;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

public class ImportDataToSequence {

	Pipe pipe;

    public ImportDataToSequence() {
        pipe = buildPipe();
    }

    public Pipe buildPipe() {
        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

        // Read data from File objects
        pipeList.add(new Input2CharSequence("UTF-8"));

        // Regular expression for what constitutes a token.
        //  This pattern includes Unicode letters, Unicode numbers, 
        //   and the underscore character. Alternatives:
        //    "\\S+"   (anything not whitespace)
        //    "\\w+"    ( A-Z, a-z, 0-9, _ )
        //    "[\\p{L}\\p{N}_]+|[\\p{P}]+"   (a group of only letters and numbers OR
        //                                    a group of only punctuation marks)
        Pattern tokenPattern = Pattern.compile("[\\p{L}\\p{N}_]+");

        // Pipes: lowercase, tokenize, remove stopwords, map to features
//        pipeList.add( new CharSequenceLowercase() );   
        
        // Tokenize raw strings
        pipeList.add(new CharSequence2TokenSequence(tokenPattern));
//        pipeList.add( new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")) );

        // Normalize all tokens to all lowercase
        pipeList.add(new TokenSequenceLowercase());

        // Remove stopwords from a standard English stoplist.
        //  options: [case sensitive] [mark deletions]
        pipeList.add(new TokenSequenceRemoveStopwords(false, false));
//        File stops = FileFactory.generateFileInstance("stoplists/"+language+".txt");
//      pipeList.add( new TokenSequenceRemoveStopwords(stops, "UTF-8", false, false, false) );     
        
        // Rather than storing tokens as strings, convert them to integers by looking them up in an alphabet.
        pipeList.add(new TokenSequence2FeatureSequence());

        return new SerialPipes(pipeList);
    }

    public InstanceList readDirectory(File directory) {
        
        // Construct a file iterator, starting with the specified directories, and recursing through subdirectories.
        // The second argument specifies a FileFilter to use to select files within a directory.
        // The third argument is a Pattern that is applied to the filename to produce a class label. In this case, I've asked it to use the last directory name in the path.
        FileIterator iterator = new FileIterator(directory, (FileFilter) new TxtFilter(), FileIterator.LAST_DIRECTORY);

        // Construct a new instance list, passing it the pipe we want to use to process instances.
        InstanceList instances = new InstanceList(pipe);
        
        // Now process each instance provided by the iterator.
        instances.addThruPipe(iterator);

        return instances;
    }

    public static void main (String[] args) throws IOException {

        ImportDataToSequence importer = new ImportDataToSequence();
        InstanceList instances = importer.readDirectory(new File(args[0]));
        instances.save(new File("recursos/file1_salida.txt"));

    }
    
}
