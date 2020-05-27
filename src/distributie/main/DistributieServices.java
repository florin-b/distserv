package distributie.main;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import distributie.beans.Borderou;
import distributie.beans.Login;
import distributie.beans.Masina;
import distributie.beans.PozitieClient;
import distributie.beans.Sofer;
import distributie.beans.Traseu;
import distributie.beans.TraseuBorderou;
import distributie.beans.User;
import distributie.database.UserDAO;
import distributie.model.BorderouriService;
import distributie.model.CalculeazaTraseu;
import distributie.model.Localizare;
import distributie.model.OperatiiFiliala;
import distributie.model.OperatiiGps;
import distributie.model.OperatiiSoferi;
import distributie.model.OperatiiTablete;
import distributie.model.OperatiiTraseu;
import distributie.model.Soferi;
import distributie.utils.MailOperations;
import distributie.utils.Utils;

@Path("/distributie")
public class DistributieServices {

	@Path("/login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(String params) {

		User user = null;

		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(params);
			Login login = new Login((String) jsonObject.get("userName"), (String) jsonObject.get("password"));
			user = new UserDAO().validateUser(login);

		} catch (ParseException e) {
			MailOperations.sendMail(Utils.getStackTrace(e));
		}

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(user).build();
	}

	@Path("/soferi")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSoferi(@QueryParam("filiala") String filiala) {

		List<Sofer> listSoferi = new Soferi().getListSoferi(filiala);

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(listSoferi).build();
	}

	@Path("/borderouri")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBorderouri(String params) {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = (JSONObject) new JSONParser().parse(params);
		} catch (ParseException e) {
			MailOperations.sendMail(Utils.getStackTrace(e));
		}

		List<Borderou> listBorderouri = new BorderouriService().getBorderouri((String) jsonObject.get("codSofer"),
				(String) jsonObject.get("dataStart"), (String) jsonObject.get("dataStop"));

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(listBorderouri).build();
	}

	@GET
	@Path("/activitateBord")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTraseuBorderou(@QueryParam("borderou") String borderou) {

		Traseu traseu;

		CalculeazaTraseu calculeaza = new CalculeazaTraseu();
		traseu = calculeaza.getEvenimenteTraseu(borderou);

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(traseu).build();
	}

	@GET
	@Path("/hartaBord")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHartaBorderou(@QueryParam("borderou") String borderou, @QueryParam("dataStart") String dataStart,
			@QueryParam("dataStop") String dataStop) {

		OperatiiTraseu operatiiTraseu = new OperatiiTraseu();

		List<PozitieClient> pozitiiClienti = operatiiTraseu.getCoordClientiBorderou(borderou);
		List<TraseuBorderou> traseuBorderou = operatiiTraseu.getTraseuBorderou(borderou, dataStart, dataStop);

		StringBuilder strPozitii = new StringBuilder();

		for (PozitieClient client : pozitiiClienti) {
			strPozitii.append(client.getLatitudine());
			strPozitii.append(",");
			strPozitii.append(client.getLongitudine());
			strPozitii.append(",");
			strPozitii.append(client.getNumeClient());
			strPozitii.append("#");
		}

		StringBuilder strTraseu = new StringBuilder();

		for (TraseuBorderou traseu : traseuBorderou) {
			if (traseu.getViteza() > 0) {
				strTraseu.append(traseu.getLatitudine());
				strTraseu.append(",");
				strTraseu.append(traseu.getLongitudine());
				strTraseu.append("#");
			}
		}

		StringBuilder strResult = new StringBuilder();
		strResult.append(strTraseu.toString());
		strResult.append("--");
		strResult.append(strPozitii.toString());

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(strResult).build();

	}

	@GET
	@Path("/masini")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMasiniFiliala(@QueryParam("filiala") String filiala) {

		List<Masina> listMasini = new OperatiiFiliala().getMasiniFiliala(filiala);

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(listMasini).build();
	}

	@GET
	@Path("/localizare")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocalizareMasini(@QueryParam("masini") String masini) {

		String listMasini = "'" + masini.replace(",", "','").replace("\"", "") + "'";

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(new Localizare().getPozitieMasini(listMasini))
				.build();
	}

	@GET
	@Path("/traseu")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTraseuMasini(@QueryParam("masina") String masina, @QueryParam("startI") String startI,
			@QueryParam("stopI") String stopI) {

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600")
				.entity(new OperatiiTraseu().getTraseuInterval(masina, startI, stopI)).build();
	}

	@GET
	@Path("/gps")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGpsInactiv(@QueryParam("filiala") String filiala) {

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(new OperatiiGps().getGpsInactiv(filiala)).build();
	}

	@GET
	@Path("/tablete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTabletaSofer(@QueryParam("codSofer") String codSofer) {

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(new OperatiiTablete().getTableteSoferi(codSofer))
				.build();
	}

	@GET
	@Path("/gestiune")
	@Produces(MediaType.APPLICATION_JSON)
	public Response opereazaTableta(@QueryParam("codTableta") String codTableta,
			@QueryParam("codSofer") String codSofer, @QueryParam("operatie") String operatie) {

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600")
				.entity(new OperatiiTablete().gestioneazaCod(codTableta, codSofer, operatie)).build();
	}

	@GET
	@Path("/livrari")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLivrari(@QueryParam("codSofer") String codSofer) {
		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600")
				.entity(new OperatiiSoferi().getClientiBordNelivrati(codSofer)).build();
	}

	@GET
	@Path("/test")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers() {

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity("Evrika!").build();
	}

}
