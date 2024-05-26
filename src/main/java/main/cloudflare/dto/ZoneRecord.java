package main.cloudflare.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZoneRecord {
	@JsonProperty
	private String id;
	@JsonProperty("zone_id")
	private String zoneId;
	@JsonProperty("zone_name")
	private String zoneName;
	@JsonProperty
	private String name;
	@JsonProperty
	private String type;
	@JsonProperty
	private String content;
	@JsonProperty
	private boolean proxiable;
	@JsonProperty
	private boolean proxied;
	@JsonProperty
	private int ttl;
	@JsonProperty
	private boolean locked;
	@JsonProperty("created_on")
	private String createdOn;
	@JsonProperty("modified_on")
	private String modifiedOn;
}
