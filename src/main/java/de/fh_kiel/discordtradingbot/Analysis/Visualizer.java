package de.fh_kiel.discordtradingbot.Analysis;

import de.fh_kiel.discordtradingbot.Config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


public class Visualizer {

	public Visualizer() {

	}

	public void visualizeHistory(char c) throws IOException {

		Evaluator.LetterStatisticsItem data = Evaluator.getInstance().getLetterStatistics().get(Config.charToIndex(c));

		final File file = new File("src\\main\\svg\\svgbase.svg");
		File newfile = new File("src\\main\\svg\\temp.svg");

		//basis svg kopieren
		Path filepath = file.toPath();
		Path newfilepath = newfile.toPath();
		Files.copy(filepath, newfilepath, REPLACE_EXISTING);

		//datenpunkte in neues svg eintragen
		FileWriter fw = new FileWriter(newfile);
		fw.write("<text x=\"80\" y=\"15\">" + data.getMaxValue() + "</text>" +
						"<text x=\"80\" y=\"135\">" + (((double) data.getMaxValue()) / 3) * 2 + "</text>" +
						"<text x=\"80\" y=\"255\">" + ((double) data.getMaxValue()) / 3 + "</text>" +
						"<text x=\"80\" y=\"375\">0</text>" +
						"<text x=\"50\" y=\"200\" class=\"label-title\">Value of " + data.getLetter() + "</text> </g>" +
						"<g class=\"data\">");
		int i;
		if (data.getTradedLetterValues().size() > 13) i = data.getTradedLetterValues().size() - 13;
		else i = 0;
		for (; i < data.getTradedLetterValues().size(); i++) {
			if (data.getTradedLetterValues().size() > 13) i = data.getTradedLetterValues().size() - 13;
			fw.write(  "<circle cx=\"" + 100+50*i + "\" cy=\"" + data.getTradedLetterValues().get(i) +"\" r=\"4\"></circle>");
		}
		fw.write("</g></svg>");
		fw.close();

		//svg an channelinteracter senden
		//todo

	}
}