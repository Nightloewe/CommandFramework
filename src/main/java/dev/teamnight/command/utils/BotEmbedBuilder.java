package dev.teamnight.command.utils;

import java.awt.Color;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.function.Consumer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

public class BotEmbedBuilder extends EmbedBuilder {

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
