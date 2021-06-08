package de.fh_kiel.discordtradingbot.Analysis;

import de.fh_kiel.discordtradingbot.Config;
import de.fh_kiel.discordtradingbot.Interaction.EventItem;
import de.fh_kiel.discordtradingbot.Interaction.EventListener;
import de.fh_kiel.discordtradingbot.Interaction.EventType;
import de.fh_kiel.discordtradingbot.ZuluBot;

import java.io.*;
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
		FileWriter fw = new FileWriter(newfile, true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("  <g\n" +
				"     class=\"labels y-labels\"\n" +
				"     id=\"g32\"\n" +
				"     transform=\"translate(-20)\">\n" +
				"    <text\n" +
				"       x=\"80\"\n" +
				"       y=\"15\"\n" +
				"       id=\"text22\">" + data.getMaxValue() + "</text>\n" +
				"    <text\n" +
				"       x=\"80\"\n" +
				"       y=\"135\"\n" +
				"       id=\"text24\">" + (int) ((((double) data.getMaxValue()) / 3) * 2) + "</text>\n" +
				"    <text\n" +
				"       x=\"80\"\n" +
				"       y=\"255\"\n" +
				"       id=\"text26\">" + (int) (((double) data.getMaxValue()) / 3) + "</text>\n" +
				"    <text\n" +
				"       x=\"80\"\n" +
				"       y=\"375\"\n" +
				"       id=\"text28\">0</text>\n" +
				"    <text\n" +
				"       x=\"50\"\n" +
				"       y=\"200\"\n" +
				"       class=\"label-title\"\n" +
				"       id=\"text30\">Value of " + data.getLetter().getLetter() + "</text>\n" +
				"  </g>\n" +
				"  <g\n" +
				"     class=\"data\"\n" +
				"     id=\"g36\"\n" +
				"     style=\"fill:#ff0000;fill-opacity:1\">\n");
		int i;
		if (data.getTradedLetterValues().size() > 13) i = data.getTradedLetterValues().size() - 13;
		else i = 0;
		for (; i < data.getTradedLetterValues().size(); i++) {
			bw.write("    <circle\n" +
					"       cx=\"" + (100 + 50 * i) + "\"\n" +
					"       cy=\"" + ((int) (375 - (((double) data.getTradedLetterValues().get(i) / data.getMaxValue()) * 360))) + "\"\n" +
					"       r=\"4\"\n" +
					"       id=\"circle\"\n" +
					"       style=\"fill:#ff0000;fill-opacity:1\" />\n");
		}
		bw.write("</g>\n</svg>");
		bw.close();
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