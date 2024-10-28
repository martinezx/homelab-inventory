package org.xmdf.homelabinventory;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HomelabInventoryApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	void shouldReturnHateoasFormatWhenListIsRequested() {
		ResponseEntity<String> response = restTemplate
				.getForEntity("/devices", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());

		int nodeCount = documentContext.read("$.length()");
		assertThat(nodeCount).isEqualTo(3);

		Map<String, Object> embedded = documentContext.read("$._embedded");
		assertThat(embedded).size().isEqualTo(1);
		assertThat(embedded).containsKey("devices");

		Map<String, Object> links = documentContext.read("$._links");
		assertThat(links).size().isEqualTo(1);
		assertThat(links).containsKey("self");

		Map<String, Object> page = documentContext.read("$.page");
		assertThat(page).size().isEqualTo(4);
		assertThat(page).containsKeys("size", "totalElements", "totalPages", "number");
	}

	@Test
	void shouldReturnPaginationLinksWhenPageIsRequested() {
		ResponseEntity<String> response = restTemplate
				.getForEntity("/devices?size=1&page=2", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());

		Map<String, Object> links = documentContext.read("$._links");
		assertThat(links).size().isEqualTo(5);
		assertThat(links).containsKeys("self", "first", "prev", "next", "last");
	}

	@Test
	void shouldReturnAllDevicesWhenListIsRequested() {
		ResponseEntity<String> response = restTemplate
				.getForEntity("/devices", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());

		int deviceCount = documentContext.read("$._embedded.devices.length()");
		assertThat(deviceCount).isEqualTo(4);

		JSONArray ids = documentContext.read("$._embedded.devices..id");
		assertThat(ids).containsExactlyInAnyOrder(1, 2, 3, 4);

		JSONArray names = documentContext.read("$._embedded.devices..name");
		assertThat(names).containsExactlyInAnyOrder("Device 1", "Device 2", "Device 3", "Device 4");

		JSONArray amounts = documentContext.read("$._embedded.devices..brand");
		assertThat(amounts).containsExactlyInAnyOrder("Brand 1", "Brand 1", "Brand 2", "Brand 2");
	}

	@Test
	void shouldReturnAPageOfDevices() {
		ResponseEntity<String> response = restTemplate
				.getForEntity("/devices?page=0&size=1", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray page = documentContext.read("$._embedded.devices[*]");
		assertThat(page.size()).isEqualTo(1);
	}

	@Test
	void shouldReturnASortedPageOfDevices() {
		ResponseEntity<String> response = restTemplate
				.getForEntity("/devices?page=0&size=1&sort=name,desc", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray read = documentContext.read("$._embedded.devices[*]");
		assertThat(read.size()).isEqualTo(1);

		String name = documentContext.read("$._embedded.devices[0].name");
		assertThat(name).isEqualTo("Device 4");
	}

	@Test
	void shouldReturnASortedPageOfDevicesWithNoParametersAndUseDefaultValues() {
		ResponseEntity<String> response = restTemplate
				.getForEntity("/devices", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray page = documentContext.read("$._embedded.devices[*]");
		assertThat(page.size()).isEqualTo(4);

		JSONArray amounts = documentContext.read("$._embedded.devices..name");
		assertThat(amounts).containsExactly("Device 1", "Device 2", "Device 3", "Device 4");
	}
}