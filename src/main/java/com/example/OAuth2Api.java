import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

/**
 * Created by Ranga Pasumarti on 09/16/18.
 */
public class OAuth2Api {

	public String getOAuthToken() throws Exception {
		HttpClient httpClient = HttpClientBuilder.create().build();
		//HttpClient httpClient = HttpClientBuilder.create().useSystemProperties().build();
		URIBuilder uriBuilder = null;
		try {
			uriBuilder = new URIBuilder("");
			uriBuilder.setParameter("client_id", "")
					.setParameter("client_secret", "")
					.setParameter("grant_type", "client_credentials").setParameter("scope", "READ");
			HttpGet getRequest = new HttpGet("https://devapis.equifax.com/eqx-gcp-common-oauthprovider/oauth/generatetoken");
			HttpResponse response = httpClient.execute(getRequest);
			if (response.getEntity() != null) {
				String json_String = EntityUtils.toString(response.getEntity());
				Gson gson = new Gson();
				TokenResponse tokenResponse = gson.fromJson(json_String, TokenResponse.class);
				return tokenResponse.getAccess_token();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Exception occured during token request" + e.getMessage());
		}
		return null;
	}

	private void downloadFile(String documentId, String token, String fileName) throws Exception {
		try {
			CloseableHttpClient client = HttpClientBuilder.create().build();
			URIBuilder builder = null;
			builder = new URIBuilder(
					"https://xx.xx.com/document-archival/api/ZYVIK-1/document/" + documentId);
			HttpGet request = new HttpGet(builder.build());
			request.setHeader("Authorization", "Bearer " + token);
			HttpResponse response;
			response = client.execute(request);
			// response.setHeader("Content-Type", "application/force-download");
			// response.setHeader("Content-Disposition", "attachment; filename=sample.pdf");
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				File downloadFile = new File(fileName);
				// long len = entity.getContentLength();
				try (FileOutputStream outputstream = new FileOutputStream(downloadFile)) {
					entity.writeTo(outputstream);
				}
			}

			int responseCode = response.getStatusLine().getStatusCode();
			System.out.println("Request Url: " + request.getURI());
			System.out.println("Response Code: " + responseCode);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MuleConnectionException("Mule Exception issue:  ", e);
		}
	}

	public static void main(String[] arg) {
		DocumentArchivalService vHelloMessageService = new DocumentArchivalService();
		try {
			String token = vHelloMessageService.getOAuthToken();
			System.out.println("token= " + token);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		OAuth2Api vOAuth2MuleApiClient = new OAuth2Api();
		String token = vOAuth2MuleApiClient.getOAuthToken();
		System.out.println(token);
	}
