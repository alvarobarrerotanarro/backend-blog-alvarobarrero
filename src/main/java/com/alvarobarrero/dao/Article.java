package com.alvarobarrero.dao;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Article {
	private int id;
	private String title;
	private String summary;
	private String body;
	private List<String> tags;
	private String category;
	private int readingMinutes;
	private String metaDescription;
	private String seoKeywords;
	private String urlPath;
	private ArticleStatus status;
	private int viewsCount;
	private int likesCount;
	private int sharesCount;
	private Date timeCreated;
	private Date lastModification;

	public Article() {
		title = "";
		summary = "";
		body = "";
		tags = new ArrayList<>();
		category = "";
		metaDescription = "";
		seoKeywords = "";
		urlPath = "";
		status = ArticleStatus.DRAFT;
		timeCreated = Date.from(Instant.now());
		lastModification = Date.from(Instant.now());
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getReadingMinutes() {
		return readingMinutes;
	}

	public void setReadingMinutes(int readingMinutes) {
		this.readingMinutes = readingMinutes;
	}

	public String getMetaDescription() {
		return metaDescription;
	}

	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}

	public String getSeoKeywords() {
		return seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	public ArticleStatus getStatus() {
		return status;
	}

	public void setStatus(ArticleStatus status) {
		this.status = status;
	}

	public int getViewsCount() {
		return viewsCount;
	}

	public void setViewsCount(int viewsCount) {
		this.viewsCount = viewsCount;
	}

	public int getLikesCount() {
		return likesCount;
	}

	public void setLikesCount(int likesCount) {
		this.likesCount = likesCount;
	}

	public int getSharesCount() {
		return sharesCount;
	}

	public void setSharesCount(int sharesCount) {
		this.sharesCount = sharesCount;
	}

	public Date getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	public Date getLastModification() {
		return lastModification;
	}

	public void setLastModification(Date lastModification) {
		this.lastModification = lastModification;
	}

	@Override
	public String toString() {
		return String.format(
				"Article [title=%s, summary=%s, body=%s, tags=%s, category=%s, readingMinutes=%s, metaDescription=%s, seoKeywords=%s, urlPath=%s, status=%s, viewsCount=%s, likesCount=%s, sharesCount=%s, timeCreated=%s, lastModification=%s]",
				title, summary, body, tags, category, readingMinutes, metaDescription, seoKeywords, urlPath, status,
				viewsCount, likesCount, sharesCount, timeCreated, lastModification);
	}
}
