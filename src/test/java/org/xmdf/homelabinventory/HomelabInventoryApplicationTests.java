package org.xmdf.homelabinventory;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.xmdf.homelabinventory.domain.Device;

import java.net.URI;
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

	@Test
	void shouldReturnADeviceWhenDataIsSaved() {
		ResponseEntity<String> response = restTemplate.getForEntity("/devices/1", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		Number id = documentContext.read("$.id");
		assertThat(id).isEqualTo(1);

		String name = documentContext.read("$.name");
		assertThat(name).isEqualTo("Device 1");

		String brand = documentContext.read("$.brand");
		assertThat(brand).isEqualTo("Brand 1");
	}

	@Test
	void shouldNotReturnADeviceWithAnUnknownId() {
		ResponseEntity<String> response = restTemplate.getForEntity("/devices/256", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isBlank();
	}

	@Test
	@DirtiesContext
	void shouldCreateANewDevice() {
		String newDeviceName = "test device", newDeviceBrand = "test brand";

		Device newDevice = Device.builder().name(newDeviceName).brand(newDeviceBrand).build();
		ResponseEntity<Void> createResponse = restTemplate.postForEntity("/devices", newDevice, Void.class);
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI locationOfNewDevice = createResponse.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewDevice, String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		assertDeviceWithDocument(newDevice, documentContext);
	}

	@Test
	void shouldNotUpdateADeviceThatDoesNotExist() {
		Device unknownDevice = Device.builder().id(256L).build();
		HttpEntity<Device> request = new HttpEntity<>(unknownDevice);
		ResponseEntity<Void> response = restTemplate.exchange("/devices/256", HttpMethod.PUT, request, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DirtiesContext
	void shouldUpdateAnExistingDevice() {
		ResponseEntity<String> response2 = restTemplate
				.getForEntity("/devices", String.class);

		String deviceId = "1", newDeviceName = "test device", newDeviceBrand = "test brand";
		Device deviceUpdate = Device.builder().name(newDeviceName).brand(newDeviceBrand).build();

		HttpEntity<Device> request = new HttpEntity<>(deviceUpdate);
		ResponseEntity<Void> response = restTemplate.exchange("/devices/%s".formatted(deviceId), HttpMethod.PUT, request, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		ResponseEntity<String> getResponse = restTemplate.getForEntity("/devices/%s".formatted(deviceId), String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		assertDeviceWithDocument(Long.valueOf(deviceId), deviceUpdate, documentContext);
	}

	@Test
	void shouldNotDeleteADeviceThatDoesNotExist() {
		ResponseEntity<Void> deleteResponse = restTemplate.exchange("/devices/256", HttpMethod.DELETE, null, Void.class);
		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DirtiesContext
	void shouldDeleteAnExistingDevice() {
		ResponseEntity<Void> response = restTemplate.exchange("/devices/1", HttpMethod.DELETE, null, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		ResponseEntity<String> getResponse = restTemplate.getForEntity("/devices/1", String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	private void assertDeviceWithDocument(Device device, DocumentContext documentContext) {
		assertDeviceWithDocument(null, device, documentContext);
	}
	private void assertDeviceWithDocument(Long deviceId, Device device, DocumentContext documentContext) {
		Number id = documentContext.read("$.id");
		String name = documentContext.read("$.name");
		String brand = documentContext.read("$.brand");

		if (deviceId == null) {
			assertThat(id).isNotNull();
		} else {
			assertThat(id.longValue()).isEqualTo(deviceId);
		}
		assertThat(name).isEqualTo(device.getName());
		assertThat(brand).isEqualTo(device.getBrand());
	}
}