package de.fh_kiel.discordtradingbot.Analysis;

import de.fh_kiel.discordtradingbot.Config;
import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;
import de.fh_kiel.discordtradingbot.Interaction.EventType;
import de.fh_kiel.discordtradingbot.ZuluBot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


public class Visualizer implements EventListener {

	private volatile static Visualizer Instance;
	private final ZuluBot bot;

	public Visualizer(ZuluBot bot) {
		this.bot = bot;
	}

	public Path visualizeHistory(char c) throws IOException {

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
						"<text x=\"80\" y=\"135\">" + ((((double) data.getMaxValue()) / 3) * 2) + "</text>" +
						"<text x=\"80\" y=\"255\">" + (((double) data.getMaxValue()) / 3) + "</text>" +
						"<text x=\"80\" y=\"375\">0</text>" +
						"<text x=\"50\" y=\"200\" class=\"label-title\">Value of " + data.getLetter() + "</text> </g>" +
						"<g class=\"data\">");
		int i;
		if (data.getTradedLetterValues().size() > 13) i = data.getTradedLetterValues().size() - 13;
		else i = 0;
		for (; i < data.getTradedLetterValues().size(); i++) {
			if (data.getTradedLetterValues().size() > 13) i = data.getTradedLetterValues().size() - 13;
			fw.write(  "<circle cx=\"" + (100+50*i) + "\" cy=\"" + ((int) (375 - (((double) data.getTradedLetterValues().get(i) / data.getMaxValue()) * 360))) +"\" r=\"4\"></circle>");
		}
		fw.write("</g></svg>");
		fw.close();

		return newfilepath;
	}

	@Override
	public void update(EventItem eventItem) {
		if (eventItem.getEventType() != EventType.VISUALIZE) return;

		try {
			Path p = visualizeHistory(eventItem.getProduct()[0]);
			bot.getChannelInteracter().uploadFile(p.toFile(), eventItem.getChannel());
		} catch (IOException e) {
			System.err.println("could not access local filesystem - error" + e);
		}
	}
}