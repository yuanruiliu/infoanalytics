package ix.utils;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.apache.hadoop.util.StringUtils;
import org.junit.Test;

/**
 * This class contains tests to check that the WikiEdit class works correctly
 */
public class WikiEditTest {

	@Test
	public void parseTest() {
		WikiEdit edit = new WikiEdit();
		edit.parse("REVISION 12 19746 Anarchism 2002-02-25T15:43:11Z ip:140.232.153.45 ip:140.232.153.45 | CATEGORY Category1 Category2 | MAIN Marx Syndicalism Nihilism Gustave_de_Molinari");

		assertEquals(new Long(19746), edit.id);
		assertEquals(new Long(12), edit.articleId);
		assertEquals("Anarchism", edit.articleName);
		assertEquals("2002-02-25 15:43:11", edit.timestamp.toString());
		assertEquals("140.232.153.45", edit.userName);
		assertEquals(null, edit.userId);
		assertEquals(2, edit.categories.size());
		assertEquals(4, edit.outgoingLinks.size());
	}

	@Test
	public void parseLongLineTest() {
		WikiEdit edit = new WikiEdit();
		edit.parse("REVISION 12 3438130 Anarchism 2004-05-03T23:56:28Z Phil_Sandifer 60895 | CATEGORY | MAIN Individualist_anarchist Head_of_state Aragon Peter_Kropotkin Peter_Kropotkin Peter_Kropotkin Peter_Kropotkin Peter_Kropotkin Peter_Kropotkin Peter_Kropotkin Peter_Kropotkin Seattle_riots Hague_congress Pacifist Free_software General_strike Property Marie_Franï¿½ois_Sadi_Carnot Nestor_Makhno Anarchism_and_private_property Spanish_Civil_War Spanish_Civil_War The_Raven_(journal) Democracy Universal_suffrage Punk_rock Protest Protest Insult Insult Pacifism Pacifism Elisabeth_of_Austria Open_source List_of_anarchists List_of_anarchists Bertrand_Russell Hungarian_Revolution,_1956 The_Diggers Anarcho-pacifism Anarcho-pacifism Usenet Joseph_Conrad Science Black_bloc Black_bloc Marxist_theory Starhawk World_War_II World_War_II Voluntary_association Julius_Hay Benjamin_Tucker Benjamin_Tucker Benjamin_Tucker Justice CIA Cindy_Milstein War War War Jason_McQuinn Collectivization 1999 Anarchism_and_Marxism Luddite Riot 1936 Evolution 1900 Indymedia Emma_Goldman Emma_Goldman Authority Economics Economics State State State Antigone Bob_Black The_West 1872 Haymarket_Riot Haymarket_Riot France Anti-war Anti-war Philosophical_anarchism Anarchism_in_the_arts Anarchist_Black_Cross Anabaptist Anabaptist 1886 Christian_anarchism Christian_anarchism Christian_anarchism May_1968 Europe Hungary Thebes Gift_economy Catalunya CNT Technology Britain Gerrard_Winstanley 1970's Fifth_Estate Umanitï¿½_Nova Freedom Eric_S._Raymond World_War_I Marshall_Rosenberg Egoism 1898 Self-defense World_Economic_Forum De_Leonism File_sharing Use-value Insurrection Syndicalist Randolph_Bourne Xenophon 1894 Marx_Party Karl_Marx Ostracism English_Civil_War American_Civil_Rights_Movement French_Revolution French_Revolution French_Revolution Chaos Money World_Trade_Organization Worker_occupation Liberty Liberty Liberty The_Levellers Plato Historical_materialism Historical_materialism Aristippus Aristippus Courts Police Police Police Murray_Bookchin Murray_Bookchin History_of_Western_Philosophy Bombing Anarchy:_A_Journal_of_Desire_Armed Globe Spain Spain Nihilism Worship Mutual_Aid:_A_Factor_in_Evolution Mutual_Aid:_A_Factor_in_Evolution Anarchist_political_economy Fascism Utilitarianism Primitivism Leo_Tolstoy Leo_Tolstoy Lister Terrorist Deontology Cooperation Utopia Theodore_Kaczynski Ayn_Rand Libertarian_socialism Libertarian_socialism Propaganda_of_the_deed Aeschylus Aristotle An_Enquiry_Concerning_Political_Justice An_Enquiry_Concerning_Political_Justice An_Enquiry_Concerning_Political_Justice Robert_Paul_Wolff Robert_Paul_Wolff Politics Copenhagen Copenhagen Animal_rights Soviet_Union Ancient_China Judith_Malina Egotism Pierre-Joseph_Proudhon Pierre-Joseph_Proudhon Pierre-Joseph_Proudhon Pierre-Joseph_Proudhon Pierre-Joseph_Proudhon United_States_President Individualist_feminism Anarcho-primitivism 1968 Howard_Zinn Warlord Warlord Feudal Denmark Denmark Ecoterrorism Society Society Society Direct_democracy Noam_Chomsky 20th_century 20th_century 20th_century Unanimous_consent_democracy Chicago Somalia 1840s Restitution Sex_Pistols Plato's_Republic London Security Direct_action Individual Social_class 1789 Workers_Solidarity_Movement Leon_Czolgosz Liberal_democracy Frederick_Engels Anthropology Revolution Revolution Revolution Revolution Revolution Non-violence Non-violence Non-violence Free_City_of_Christiania Free_City_of_Christiania What_is_Property? What_is_Property? Eco-feminism Robert_Heinlein Ursula_K._Le_Guin Free_association Ba_Jin Freedom_anarchist_fortnightly Temple 19th_century 19th_century Marxism Marxism Marxism Industrial_action 16th_century Black_Flag_(newspaper) Ricardo_Flores_Magon 467BC God_and_the_State Dictatorship_of_the_proletariat Radical_individualism Mutualism Despotism International_Workingmen's_Association Classical_liberalism Classical_liberalism Workers_councils Workers_councils Strike_action Feminism Political_philosophy 1960's Mutual_Aid Situationism North_America North_America Anarchism_and_nature Stoic_philosophy Philosophy Philosophy Philosophy Stalinism Property_destruction History 1897 Volunteer Class_War Cynicism Trade_union Chumbawamba In_Defense_of_Anarchsim Anarcho-syndicalism Anarcho-syndicalism Consensus Taoism The_Secret_Agent Max_Stirner Max_Stirner Max_Stirner Anti-globalisation Anti-globalisation Non-violent_resistance Non-violent_resistance Ancient_Greece 1840 The_Dispossessed Atlas_Shrugged Assassination Assassination Assassination December_14 John_Zerzan John_Zerzan Group_of_Seven 1793 1793 1937 1937 Labour_movement 1901 The_Conquest_of_Bread Political_violence William_Godwin William_Godwin William_Godwin William_Godwin William_Godwin Stoicism Dialectical_materialism Treatise 17th_century Monopoly_of_force GNU Enragï¿½s Barcelona Left-wing_politics Left-wing_politics Socialization Wikiwiki Anti-capitalism Levellers Emiliano_Zapata Popular_assembly Freedom_(political) Crass Government Rioting Metropolitan_Police Linux Proletariat October_22 Green_Anarchist Past_and_present_anarchist_communities Communism Syndicalism Non-violent Buenaventura_Durruti Russian_Revolution Anarchist_communism Anarchist_communism Anarchy Anarchy Dictatorship Allen_Ginsberg Anarchism_and_democracy Athens Vandalism Vandalism English_language Political_party Anomie Lorenzo_Komboa_Ervin 404BC Vietnam_War Vietnam_War Free_market Free_Spirit Civilization Civilization Mikhail_Bakunin Mikhail_Bakunin Mikhail_Bakunin Mikhail_Bakunin Mikhail_Bakunin Mikhail_Bakunin The_Moon_is_a_Harsh_Mistress Violence Violence Mayday Communes William_McKinley Social_movement Middle_Ages Zeno_of_Citium Zeno_of_Citium Zeno_of_Citium Anarcha-feminism Anarcha-feminism Coercion Errico_Malatesta Errico_Malatesta Errico_Malatesta Anarcho-capitalism Anarcho-capitalism Anarcho-punk War_in_Iraq Red_Army IWW Umberto_I_of_Italy President_of_France Terrorism Anarchism_and_sex");

		assertEquals(0, edit.categories.size());
		assertFalse(StringUtils.join(", ", edit.outgoingLinks).startsWith(", "));
	}

	@Test
	public void listsResetTest() {
		WikiEdit edit = new WikiEdit();

		edit.parse("REVISION 1 2 article 2002-02-25T15:43:11Z user 1 | CATEGORY cat1 cat2 | MAIN link1 link2");

		assertEquals(2, edit.categories.size());
		assertEquals(2, edit.outgoingLinks.size());

		edit.parse("REVISION 2 2 article 2002-02-25T15:45:17Z user2 2 | CATEGORY cat2 cat3 | MAIN link1 link3 link4");

		assertEquals(2, edit.categories.size());
		assertEquals(3, edit.outgoingLinks.size());
	}

}
