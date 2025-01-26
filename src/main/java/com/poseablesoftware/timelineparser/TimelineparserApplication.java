package com.poseablesoftware.timelineparser;

import atlas.Atlas;
import atlas.City;
import dev.hossain.timeline.Parser;
import dev.hossain.timeline.model.RawSignal;
import dev.hossain.timeline.model.TimelineData;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlinx.coroutines.BuildersKt;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.lang.Thread.sleep;
import static java.util.stream.Collectors.groupingBy;


//@SpringBootApplication
public class TimelineparserApplication {

	private static final WebClient wc = WebClient.builder()
			.baseUrl("https://photon.komoot.io")
			.build();

	private static final ZonedDateTime beginOf2024 = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault());
	private static final ZonedDateTime endOf2024 = ZonedDateTime.of(2024, 12, 31, 23, 59, 59, 999999999, ZoneId.systemDefault());

	private static final DecimalFormat df = new DecimalFormat("#.###");

	public static void main(String[] args) {
		//SpringApplication.run(TimelineparserApplication.class, args);

		try {
			var parser = new Parser();
			var file = new File("/Users/bradleyserbus/Documents/src/timelineparser/Timeline.json");
			TimelineData result
					= BuildersKt.runBlocking(EmptyCoroutineContext.INSTANCE,// Dispatchers.getIO()
						(scope, continuation) -> parser.parse(file, continuation));

			/*result.getSemanticSegments().stream()
				.filter(ss -> {
					boolean lastYear = ZonedDateTime.parse(ss.getStartTime()).isAfter(beginOf2024)
							&& ZonedDateTime.parse(ss.getEndTime()).isBefore(endOf2024);

					return lastYear;
				})
				.flatMap(ss -> ss.getTimelinePath().stream())
				.flatMap(tp -> {
					String[] point = tp.getPoint().split(", ");

					List<FeatureCollection.Feature> features = get(getDegrees(point[0]), getDegrees(point[1])).features;

					return features.stream()
							.map(f -> new FeatureTime(f, tp.getTime()));
				})
				.forEach(ft -> {
					System.out.println(ft.time());
					if (!ft.feature().properties.countrycode.equals("US")) {
						System.out.println(ft.feature().properties.countrycode);
					}
				});*/

			result.getSemanticSegments().stream()
					.flatMap(ss -> ss.getTimelinePath().stream())
					.flatMap(p -> {
						String[] point = p.getPoint().split(", ");

						Double lat = getDegrees(point[0]);
						Double lng = getDegrees(point[1]);

						return new Atlas()
								.withLimit(1)
								.findAll(lat, lng).stream().map(c ->
										new Output(p.getTime(), c.countryCode, c.name)
								);
					})
					.filter(output -> Objects.equals(output.city(), "Kirkland"))
					.collect(groupingBy(o -> o.dateTime().split("T")[0] + "<>" + o.countryCode() + o.city()))
					.entrySet()
					.stream()
					.sorted((a, b) -> a.getKey().compareTo(b.getKey()))
					.forEach((e)-> {
						System.out.println(e.getValue().getFirst().dateTime() + ','
								+ e.getValue().getFirst().countryCode() + ',' +  e.getValue().getFirst().city());
					});

			/*result.getRawSignals().stream()
					.map(RawSignal::getPosition)
					.filter(Objects::nonNull)
					.filter(p -> {
						boolean lastYear = ZonedDateTime.parse(p.getTimestamp()).isAfter(beginOf2024)
										&& ZonedDateTime.parse(p.getTimestamp()).isBefore(endOf2024);

						return lastYear;
					})
					.map(p -> {
						String[] point = p.getLatLng().split(", ");

						Double lat = getDegrees(point[0]);
						Double lng = getDegrees(point[1]);

						return new ParsedPosition(p, lat, lng);
					})
					.flatMap(p -> {
						List<FeatureCollection.Feature> features = get(p.lat(), p.lng()).features;

						return features.stream()
								.map(f -> new FeatureTime(f, p.position().getTimestamp()));
					})
					.forEach(ft -> {
						System.out.print('.');

						if (!ft.feature().properties.countrycode.equals("US")) {
							System.out.println(ft.time());
							System.out.println(ft.feature().properties.countrycode);
						}
					});*/

			System.out.println("-------------");

			result.getRawSignals().stream()
					.map(RawSignal::getPosition)
					.filter(Objects::nonNull)
					/*.filter(p -> {
						boolean lastYear = ZonedDateTime.parse(p.getTimestamp()).isAfter(beginOf2024)
								&& ZonedDateTime.parse(p.getTimestamp()).isBefore(endOf2024);

						return lastYear;
					})*/
					.flatMap(p -> {
						String[] point = p.getLatLng().split(", ");

						Double lat = getDegrees(point[0]);
						Double lng = getDegrees(point[1]);

						return new Atlas()
								.withLimit(1)
								.findAll(lat, lng).stream().map(c ->
										new Output(p.getTimestamp(), c.countryCode, c.name)
								);
					})
					.filter(output -> Objects.equals(output.city(), "Kirkland"))
					.collect(groupingBy(o -> o.dateTime().split("T")[0] + "<>" + o.countryCode() + o.city()))
					.entrySet()
					.stream()
					.sorted((a, b) -> a.getKey().compareTo(b.getKey()))
					.forEach((e)-> {
						System.out.println(e.getValue().getFirst().dateTime() + ','
								+ e.getValue().getFirst().countryCode() + ',' +  e.getValue().getFirst().city());
					});

		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	private static FeatureCollection get(Double lat, Double lon) {
		try { sleep(1500); } catch (InterruptedException _) {}

		return wc.get()
				.uri("/reverse?lat={lat}&lon={lon}", Map.of("lat", lat, "lon", lon))
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(FeatureCollection.class)
				.retry(3)
				.block();
	}

	private static Double getDegrees(String str) {
		String degrees = str.charAt(str.length() - 1) == '°' ?
			 str.substring(0, str.length() - 2) : str;

		return Double.parseDouble(degrees);//(double) Math.round(Double.parseDouble(degrees) * 1000d) / 1000d;
	}
}
