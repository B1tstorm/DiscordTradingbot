package de.fh_kiel.discordtradingbot.Analysis;

import de.fh_kiel.discordtradingbot.Config;
import de.fh_kiel.discordtradingbot.Interaction.ChannelInteracter;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


public class Visualizer {

	private volatile static Visualizer Instance;

	private Visualizer() {

	}

	public static Visualizer getInstance() {
		if (Instance == null) {
			synchronized (Evaluator.class) {
				if (Instance == null) {
					Instance = new Visualizer();
				}
			}
		}
		return Instance;
	}

	public void visualizeHistory(Message message) throws IOException {

		Evaluator.LetterStatisticsItem data = Evaluator.getInstance().getLetterStatistics().get(Config.charToIndex(message.getContent().split(" ")[2].toCharArray()[0]));

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





		//todo svg an channelinteracter senden
		ChannelInteracter channelInteracter = null; //dummy, todo
		//!!!!!!!!!!



		channelInteracter.uploadFile(newfile, message.getChannel().block());

	}

	public void notify(Message message) {
		if (!message.getContent().split(" ")[1].equalsIgnoreCase("visualize")) return;

		try {
			visualizeHistory(message);
		} catch (IOException e) {
			System.err.println("could not access local filesystem - error" + e);
		}
	}
}