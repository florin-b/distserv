package distributie.queries;

public class SqlQueries {

	public static String getMasiniNeincarcate() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select masina,numarb,numarb_ant from ( ");
		sqlString.append(
				" select b.masina,b.numarb,bant.numarb as numarb_ant, ROW_NUMBER() OVER(PARTITION BY b.masina,b.numarb order by b.masina,b.data_e) AS NRp ");
		sqlString.append(" from websap.borderouri b join sapprd.vttk k on b.numarb=k.tknum ");
		sqlString.append(" left join websap.borderouri bant on bant.masina=b.masina and bant.sttrg>2 ");
		sqlString.append(" where b.sttrg = 2 and k.mandt='900' AND k.tplst = :filiala and k.shtyp = '1110' ");
		sqlString.append(" and not exists(select 1 from sapprd.zsfarsitinc where document = b.numarb) ");
		sqlString.append(" order by b.masina,b.data_e) where nrp=1 ");

		return sqlString.toString();
	}
	
	
	public static String getCoordStartStopBorderou() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select a.plecare, ");
		sqlString.append(" nvl((select b.latitude from sapprd.zgpsdepcoord b, sapprd.zgpsdep c where b.id = c.gpsid and ");
		sqlString.append(" c.pct = a.plecare),'0,0') plec_lat, ");
		sqlString.append(" nvl((select b.longitude from sapprd.zgpsdepcoord b, sapprd.zgpsdep c where b.id = c.gpsid and ");
		sqlString.append(" c.pct = a.plecare),'0,0') plec_long, a.adr_plecare, ");
		sqlString.append(" a.sosire, ");
		sqlString.append(" nvl((select b.latitude from sapprd.zgpsdepcoord b, sapprd.zgpsdep c where b.id = c.gpsid and ");
		sqlString.append(" c.pct = a.sosire),'0,0') sosire_lat, ");
		sqlString.append(" nvl((select b.longitude from sapprd.zgpsdepcoord b, sapprd.zgpsdep c where b.id = c.gpsid and ");
		sqlString.append(" c.pct = a.sosire),'0,0') sosire_long, a.adr_sosire ");
		sqlString.append(" from websap.bord_ends a where a.tknum=? ");

		return sqlString.toString();

	}

	public static String getMasiniNeincarcateDepoz2() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select masina,numarb,numarb_ant, vstel2 from ( ");
		sqlString.append(" select b.masina,b.numarb,bant.numarb as numarb_ant, s.vstel2, ROW_NUMBER()   ");
		sqlString.append(" OVER(PARTITION BY b.masina,b.numarb order by b.masina,b.data_e) AS NRp ");
		sqlString.append(" from websap.borderouri b join sapprd.vttk k on b.numarb=k.tknum ");
		sqlString.append(" join sapprd.zsvttk s on s.mandt=k.mandt and s.tknum=k.tknum ");
		sqlString.append(" left join websap.borderouri bant on bant.masina=b.masina and bant.sttrg>2 ");
		sqlString.append(" where b.sttrg = 2 and k.mandt='900' and k.shtyp = '1110' and s.vstel2 = :filiala ");
		sqlString.append(" and not exists(select 1 from sapprd.zsfarsitinc where document = b.numarb and ");
		sqlString.append(" codsofer = :codAngajat ) ");
		sqlString.append(" order by b.masina,b.data_e) where nrp=1 ");

		return sqlString.toString();
	}

	public static String getUnitLogAngajat() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append("select filiala from personal  where cod=? ");

		return sqlString.toString();
	}

	public static String setSfarsitIncarcare() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" insert into sapprd.zsfarsitinc(mandt, document, codsofer, data, ora) ");
		sqlString.append(
				" values ('900',?,?, (select to_char(sysdate,'yyyymmdd') from dual), (select to_char(sysdate,'hh24mi') from dual) )");

		return sqlString.toString();

	}

	public static String setSfarsitIncImg() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" insert into zsfarsitincfoto(document, img) ");
		sqlString.append(" values (?,?) ");

		return sqlString.toString();

	}

	public static String getBorderouri() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select numarb, trunc(to_date(daten,'yyyymmdd')) dataEmitere, 'false' activ from ");
		sqlString.append(" (select v.tknum as numarb,  v.daten, p.pernr as cod_sofer ");
		sqlString.append(" from sapprd.vttk v join sapprd.vekp m on v.mandt = m.mandt and v.tknum = m.vpobjkey  ");
		sqlString.append(" and m.vpobj = '04' join sapprd.vtpa p on v.mandt = p.mandt and v.tknum = p.vbeln  ");
		sqlString.append(" and p.parvw = 'ZF' where v.mandt = '900') ");
		sqlString.append(" where cod_sofer =:codSofer and daten between :dataStart and :dataStop " + " union ");
		sqlString.append(" select numarb, trunc(a.data_e) dataEmitere, 'true' activ from borderouri a where   ");
		sqlString.append(" cod_sofer =:codSofer and data_e between to_date(:dataStart,'yyyymmdd')  ");
		sqlString.append(" and to_date(:dataStop,'yyyymmdd') order by dataEmitere ");

		return sqlString.toString();
	}
	
	public static String getEvenimenteTableta() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select client, codadresa, eveniment, data, ora,  gps, fms from sapprd.zevenimentsofer ");
		sqlString.append(" where document =? order by data, ora ");

		return sqlString.toString();
	}
	
	public static String getStareMasina() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append("select count(codsofer) from sapprd.zevenimentsofer where document =?  and  client = document and eveniment = 'S'");

		return sqlString.toString();
	}
	
	public static String getClientiBorderou() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select a.poz, c.nume, decode(a.cod_client,'', a.cod_furnizor, a.cod_client) cod_client, ");
		sqlString.append(" decode(a.cod_client,'',a.adresa_furnizor, a.adresa_client) cod_adresa,  b.city1,  nvl(b.street,' ') street,   ");
		sqlString.append(" nvl(b.house_num1,' ') house_num1, b.region from sapprd.zdocumentesms a, sapprd.adrc b, clienti c where a.nr_bord =:codBorderou  ");
		sqlString.append(" and c.cod = a.cod_client and b.client = '900' and b.addrnumber = decode(a.cod_client,'',a.adresa_furnizor, a.adresa_client) ");
		sqlString.append(" order by a.poz ");

		return sqlString.toString();
	}
	
	public static String getStartStopBorderou() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select a.plecare, a.adr_plecare, a.sosire, a.adr_sosire  from websap.bord_ends a where a.tknum=? ");

		return sqlString.toString();
	}
	
	public static String getBorderouActiv() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select numarb from ( ");
		sqlString.append(" select numarb from websap.borderouri where sttrg in ( 4, 6) ");
		sqlString.append(" and replace(masina,'-','') =? order by sttrg desc,data_e asc)  where rownum<2 ");

		return sqlString.toString();

	}
	
	public static String getTipMasina() {

		StringBuilder str = new StringBuilder();
		str.append(" select distinct user2 tipMasina from sapprd.coas where replace(ktext,'-','') =? and mandt = '900' ");

		return str.toString();

	}
	
	public static String getBorderouSofer() {
		StringBuilder str = new StringBuilder();

		str.append(" select document from sapprd.zevenimentsofer where ");
		str.append(" document = (select numarb from ( ");
		str.append(" select numarb from websap.borderouri where sttrg in( 4, 6) ");
		str.append(" and cod_sofer =? order by sttrg desc,data_e asc) x where rownum<2) ");
		str.append(" and client = document and eveniment = 'P' ");

		return str.toString();
	}
	
	public static String getClientiBordNelivrati() {
		StringBuilder str = new StringBuilder();

		str.append(" select a.cod_client, a.name1, c.city1,  nvl(c.street,' ') street,   ");
		str.append(" (select  distinct tel_number from sapprd.kna1 k, sapprd.adr2 ad, sapprd.adrt t ");
		str.append(" where k.mandt = '900' and k.kunnr = a.cod_client and k.mandt = ad.client ");
		str.append(" and k.adrnr = ad.addrnumber	and ad.client = t.client	and ad.addrnumber = t.addrnumber ");
		str.append(" and ad.consnumber = t.consnumber and t.comm_type  = 'TEL' and t.remark = 'LIVRARI' and rownum<2) tel_client,");
		str.append(" nvl(c.house_num1,' ') house_num1, c.region from sapprd.zdocumentesms a, sapprd.zevenimentsofer b, sapprd.adrc c where a.nr_bord =? ");
		str.append(" and c.client = '900' and c.addrnumber = a.adresa_client ");
		str.append(" and b.document(+) = a.nr_bord and b.client(+) = a.cod_client and b.codadresa(+) = a.adresa_client ");
		str.append(" and nvl(b.client,'-1') = '-1' and length(trim(a.name1)) > 0 order by a.poz ");

		return str.toString();

	}
	
	public static String getTraseuInterval() {
		StringBuilder sqlString = new StringBuilder();
		sqlString.append(" select x.longitude, x.latitude, x.mileage, x.speed, to_char(x.record_time,'dd.Mon.yyyy hh24:mi:ss','NLS_DATE_LANGUAGE = AMERICAN') record_time from ");
		sqlString.append(" (select longitude, latitude, mileage, speed, record_time from gps_date where device_id = ");
		sqlString.append(" (select id from gps_masini where nr_masina =?) and record_time ");
		sqlString.append(" between to_date(?,'dd-mm-yyyy HH24:mi') and to_date(?,'dd-mm-yyyy HH24:mi')  order by record_time) x ");

		return sqlString.toString();

	}

	public static String getTableteSofer() {
		StringBuilder str = new StringBuilder();
		str.append(" select codtableta, to_char(to_date(datainreg,'yyyymmdd')) datai, decode(stare,'1','Activ','Inactiv') stare ");
		str.append(" from sapprd.Ztabletesoferi where codsofer =:codsofer order by stare, datainreg desc");

		return str.toString();
	}
	
	public static String invalidateAllCodes(){
		StringBuilder str = new StringBuilder();
		str.append(" update sapprd.ztabletesoferi set stare = '0' where  codsofer =? ");
		
		return str.toString();
	}
	
	public static String addCodTableta()
	{
		StringBuilder str = new StringBuilder();
		str.append(" insert into sapprd.ztabletesoferi(mandt, codsofer, codtableta, datainreg, stare, creatde, orainreg) ");
		str.append(" values ('900', ?, ?, ?, ?, ?, ?) ");
		
		return str.toString();
	}
	
}
