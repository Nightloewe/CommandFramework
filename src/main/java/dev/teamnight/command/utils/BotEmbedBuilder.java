package dev.teamnight.command.utils;

import java.awt.Color;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

public class BotEmbedBuilder extends EmbedBuilder {

	private static Color[] rainbowColors = new Color[] {
														new Color(128, 0, 0),
														new Color(130, 40, 40),
														new Color(141, 83, 59),
														new Color(153, 102, 117),
														new Color(153, 102, 169),
														new Color(128, 0, 128),
														new Color(101, 0, 155),
														new Color(72, 0, 225),
														new Color(4, 0, 208),
														new Color(0, 68, 220),
														new Color(1, 114, 226),
														new Color(1, 159, 232),
														new Color(11, 175, 162),
														new Color(23, 179, 77),
														new Color(0, 212, 28),
														new Color(0, 255, 0),
														new Color(128, 255, 0),
														new Color(200, 255, 0),
														new Color(255, 255, 0),
														new Color(255, 219, 0),
														new Color(255, 182, 0),
														new Color(255, 146, 0),
														new Color(255, 109, 0),
														new Color(255, 73, 0),
														new Color(255, 0, 0),
														new Color(255, 0, 128),
														new Color(255, 105, 180),
														new Color(255, 0, 255),
														new Color(168, 0, 185)
														};
	private static boolean rainbowColorsEnabled = false;
	private static Random rand = new Random();
	
	private boolean colorSet = false;
	
	public static void setRainbowColorsEnabled(boolean enabled) {
		BotEmbedBuilder.rainbowColorsEnabled = true;
	}
	
	public BotEmbedBuilder withOkColor() {
		this.setColor(Color.GREEN);
		
		return this;
	}
	
	public BotEmbedBuilder withErrorColor() {
		this.setColor(Color.RED);
		
		return this;
	}
	
	public void sendMessage(MessageChannel channel) {
		channel.sendMessage(this.build()).queue();
	}
	
	public void sendMessage(MessageChannel channel, Consumer<Message> success) {
		channel.sendMessage(this.build()).queue(success);
	}
	
	public void sendMessage(MessageChannel channel, Consumer<Message> success, Consumer<Throwable> failure) {
		channel.sendMessage(this.build()).queue(success, failure);
	}
	
	@Override
	public BotEmbedBuilder addBlankField(boolean inline) {
		super.addBlankField(inline);
		
		return this;
	}

	@Override
	public MessageEmbed build() {
		if(!this.colorSet && rainbowColorsEnabled) {
			int index = rand.nextInt(rainbowColors.length);
			
			Color c = rainbowColors[index];
			
			this.setColor(c);
		}
		
		return super.build();
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty();
	}

	@Override
	public BotEmbedBuilder setTitle(String title) {
		super.setTitle(title);
		
		return this;
	}

	@Override
	public BotEmbedBuilder setTitle(String title, String url) {
		super.setTitle(title, url);
		
		return this;
	}

	@Override
	public StringBuilder getDescriptionBuilder() {
		return super.getDescriptionBuilder();
	}

	public BotEmbedBuilder setDescription(String description) {
		super.setDescription(description);
		
		return this;
	}

	@Override
	public BotEmbedBuilder appendDescription(CharSequence description) {
		super.appendDescription(description);
		
		return this;
	}

	@Override
	public BotEmbedBuilder setTimestamp(TemporalAccessor temporal) {
		super.setTimestamp(temporal);
		
		return this;
	}

	@Override
	public BotEmbedBuilder setColor(Color color) {
		super.setColor(color);
		this.colorSet = true;
		
		return this;
	}

	@Override
	public BotEmbedBuilder setThumbnail(String url) {
		super.setThumbnail(url);
		
		return this;
	}

	@Override
	public BotEmbedBuilder setImage(String url) {
		super.setImage(url);
		
		return this;
	}

	@Override
	public BotEmbedBuilder setAuthor(String name, String url, String iconUrl) {
		super.setAuthor(name, url, iconUrl);
		
		return this;
	}

	@Override
	public BotEmbedBuilder setFooter(String text, String iconUrl) {
		super.setFooter(text, iconUrl);
		
		return this;
	}

	@Override
	public BotEmbedBuilder addField(Field field) {
		super.addField(field);
		
		return this;
	}

	@Override
	public BotEmbedBuilder addField(String name, String value, boolean inline) {
		super.addField(name, value, inline);
		
		return this;
	}

	@Override
	public BotEmbedBuilder clearFields() {
		super.clearFields();
		
		return this;
	}

	@Override
	public List<Field> getFields() {
		return super.getFields();
	}
	
}
