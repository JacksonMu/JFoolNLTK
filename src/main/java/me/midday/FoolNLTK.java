package me.midday;


import me.midday.lexical.LSTMLexicalAnalyzer;
import me.midday.lexical.LexicalAnalyzer;
import me.midday.lexical.TFPredictor;
import me.midday.lexical.Vocab;
import org.apache.commons.io.IOUtils;


import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;


public class FoolNLTK {
    private static LexicalAnalyzer defaultLexicalAnalyzer = null;

    private static TFPredictor createPredictor(String name, int classNum) throws IOException {
        TFPredictor model;
		try (InputStream inputStream = FoolNLTK.class.getResourceAsStream(name)) {
			byte[] segb = IOUtils.toByteArray(inputStream);
			model = new TFPredictor(segb, classNum);
		} 
        return model;
    }

    public static LexicalAnalyzer getLSTMLexicalAnalyzer() {
        if (defaultLexicalAnalyzer !=null ){
            return defaultLexicalAnalyzer;
        }

//        LexicalAnalyzer lexicalAnalyzer = null;
        try(InputStream mapIn = FoolNLTK.class.getResourceAsStream("/nmodels/all_map.json")) {
            Vocab vocab = new Vocab(mapIn);
            TFPredictor segModel = createPredictor("/nmodels/seg.pb", vocab.getSegLabelNum());
            TFPredictor posModel = createPredictor("/nmodels/pos.pb", vocab.getPosLabelNum());
            TFPredictor nerModel = createPredictor("/nmodels/ner.pb", vocab.getNerLabelNum());
            defaultLexicalAnalyzer = new LSTMLexicalAnalyzer(vocab, segModel, posModel, nerModel);
        }catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultLexicalAnalyzer;
    }

}
