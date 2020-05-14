//Importo i package necessari
import javax.imageio.ImageIO;
import java.io.*;
import java.text.*; 
import java.util.*; 
import java.net.*;
import java.sql.*;
import java.util.Date;



public class protocollo_server {




	/*public void start() throws IOException 
    {
        String clientSentence;
        String capitalizedSentence;
        ServerSocket welcomeSocket = new ServerSocket(5554); //Attivo la Socket sul Server in ascolto sulla porta 5554
        Socket connectionSocket;

        while (true)
        {

             //Preparazione delle informazioni da ricevere

             System.out.println("In attesa di chiamate dai Client... ");
             connectionSocket = welcomeSocket.accept();
             DataInputStream dis = new DataInputStream(s.getInputStream()); 
             DataOutputStream dos = new DataOutputStream(s.getOutputStream());

         }
    }
	 */

	public static void main(String[] args) throws IOException  
    { 
        // server is listening on port 6789
        ServerSocket ss = new ServerSocket(8080);
        Thread t[] = new Thread[300];
        int number_client_connected = 0;
        // running infinite loop for getting 
        // client request 
        while (true)  
        { 
            Socket s = null; 
              
            try 
            { 
                // socket object to receive incoming client requests 
                s = ss.accept(); 
                  
                System.out.println("A new client is connected : " + s); 
                  
                // obtaining input and out streams 
                //DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                BufferedReader dis = new BufferedReader(new InputStreamReader(s.getInputStream()));


                System.out.println("Assigning new thread for this client"); 
  
                // create a new thread object 
                t[number_client_connected] = new ClientHandler(s, dis, dos, number_client_connected);


                // Invoking the start() method 
                t[number_client_connected].start();

                number_client_connected++;
                  
            } 
            catch (Exception e){ 
                s.close(); 
                e.printStackTrace(); 
            } 
        } 
    } 
} 
  
// ClientHandler class 
class ClientHandler extends Thread  
{
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;




    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd"); 
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss"); 
    //final DataInputStream dis;
    final BufferedReader dis;
    final DataOutputStream dos; 
    final Socket s;
    final int number;




  
    // Constructor 
    public ClientHandler(Socket s, BufferedReader dis, DataOutputStream dos, int number)
    {
        this.number = number;
        this.s = s; 
        this.dis = dis; 
        this.dos = dos; 
    } 
  
    @Override
    public void run()  
    { 
        String received; 
        String username = "", password = "", send_username = "", _message = "";
        String toreturn = "";
        boolean is_connect = false;
        boolean is_disconnect = true;
        boolean is_closed = false;
        boolean find_user = false;
        boolean my_bool = false;
        int id = 0;
        int client_id = 0;
        int send_id = 0;
        String variable_return = "1";
        int number_users_online = 0;

        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs_temp = null;
        Connection conn = null;

        Statement stmt_temp = null;
        Connection conn_temp = null;




        int number_sessions = 0;
        int number_user_online_session = 0;
        int number_characters = 0;
        int number_items_b = 0;
        int number_categories = 0;
        int number_object_categories = 0;

        String id_passed_session = "";
        String id_passed_character = "";

        String filter_item = "";

        int i = 0;
        int x = 0;
        boolean is_session_connected = false;
        boolean is_character_connected = false;
        boolean is_host = false;

        String c_name = "", c_desc = "";
        boolean find_category = false;

        Blob image = null;

        int session_id = 0;
        int character_id = 0;
        int category_id = 0;


        String trade_name_c = "";
        int number_item_trade_1 = 0;
        int number_item_trade_2 = 0;
        String[] trade_o_1 = new String[100];
        String[] trade_o_2 = new String[100];
        int[] trade_q_1 = new int[100];
        int[] trade_q_2 = new int[100];

        int[] quantity_items_1 = new int[100];
        int[] quantity_items_2 = new int[100];

        int[] id_items_1 = new int[100];
        int[] id_items_2 = new int[100];

        boolean is_null_1 = false;
        boolean is_null_2 = false;

        boolean find_trade = false;

        boolean is_o1_ok = false;
        boolean is_o2_ok = false;


        String character_nome = "", character_biografia = "", character_razza = "";
        int character_hp = 0, character_eta = 0, character_hp_max;
        float character_peso = 0, character_altezza = 0;
        String what_character = "";


        String new_username = "", new_password = "", new_name = "", new_surname = "", new_email = "";


        String s_titolo = "", s_sottotitolo = "";
        int s_codice_invito = 0;


        String o_nome = "", o_descrizione = "", o_campo1 = "", o_campo2 = "", o_campo3 = "", o_campo4 = "", o_campo5 = "", o_rarita = "";
        float o_valore = 0;

        boolean find_object = false, find_character = false;
        int ob_id = 0, ch_id = 0, ob_quantity = 0, ob_numbers = 0;
        String ob_data = "", ob_type = "";

        boolean is_trading_enabled1 = false;
        boolean is_trading_enabled2 = false;

        String trade_c_name1 = "", trade_c_name2 = "";
        int trade_c_id1 = 0;
        int trade_c_id2 = 0;
        boolean find_trade1 = false;
        boolean find_trade2 = false;

        while (is_closed == false)
        { 
            try { 
                   
                // receive the answer from client 
                received = dis.readLine();
                if(received.equals("connect"))      //CONNECT
                {  	
                	if(is_connect == false && is_disconnect == true)
                	{

	                	//CONNESSIONE AL DATABASE
	
	                    System.out.println("\nClient " + this.s + " want to connect to the database");
	                    username = dis.readLine();
	                    password = dis.readLine();
	
	
	
	
	                    try {
	                        Class.forName("com.mysql.jdbc.Driver");
	                        conn = DriverManager.getConnection("jdbc:mysql://216.158.239.6/game?" + "user=inventorymaster&password=Rootinventory1!");
	
	
	                        // creo la tabella
	                        stmt = conn.createStatement();
	
	                        /*
	                        // inserisco due record
	                        pstmt = conn.prepareStatement("INSERT INTO utenti " + "(nome, cognome, email, nome_utente, password) values (?)");
	
	                        pstmt.setString(1, "Mario");
	                        pstmt.setString(2, "Rossi");
	                        pstmt.setString(3, "ciccio@ciao123.it");
	                        pstmt.setString(4, "maro");
	                        pstmt.setString(5, "maro123");
	                        pstmt.execute();
	
	                        System.out.println("id: " + rs.getString("id"));
	                        System.out.println("firstname: " + rs.getString("firstname"));
	                        System.out.println("lastname: " + rs.getString("lastname"));
	                        */
	
	                        find_user = false;
	
	                        // recupero i dati
	                        rs = stmt.executeQuery("SELECT * from Utenti where Utenti.nome_utente = " + username);

	                        //Controllo nel database dell'esistenza dell'utente e assegnazione di quest'ultimo ad 1 dello stato di login
	
	                        while(rs.next() && find_user == false)
	                        {
	                            if(username.equals(rs.getString("nome_utente")) && password.equals(rs.getString("password")))
	                            {
	                                find_user = true;
	                                client_id = rs.getInt("id");
                                    id = rs.getInt("id");
	
	                            }
	
	                        }
	
	                        if(find_user)
	                        {
	                            number_sessions = 0;
	                            //Aggiungo caricamento sessioni
                                System.out.println("\nLoading Sessions: " + client_id);
                                rs = stmt.executeQuery("SELECT * from R_Utente_Sessione rus where rus.id_utente = " + client_id);
                                while (rs.next()) {


                                    number_sessions++;

                                }


	                            System.out.println("ID: " + client_id);
	                            pstmt = conn.prepareStatement("UPDATE Utenti SET stato_login = '1' where Utenti.id = " + client_id);
	                            pstmt.execute();
	                            pstmt.close(); // rilascio le risorse
                                is_connect = true;
                                is_disconnect = false;
	                            variable_return = "1";



	                        }
	
	                        else
	                        {
	                            variable_return = "-2";
                                is_connect = false;
                                is_disconnect = true;

	                        }
	
	                        //pstmt.close(); // rilascio le risorse
	                        /*stmt.close(); // rilascio le risorse
	                        conn.close(); // termino la connessione*/


	                        System.out.println(variable_return);
	                        dos.writeBytes(variable_return + '\n');


                            dos.writeBytes(Integer.toString(number_sessions) + '\n');

	                        if(number_sessions != 0)
                            {
                                rs = stmt.executeQuery("SELECT * from Sessioni s inner join R_Utente_Sessione rus on rus.id_sessione = s.id where rus.id_utente = " + client_id);
                                while (rs.next()) {

                                    //Invio del titolo
                                    dos.writeBytes(rs.getString("titolo") + '\n');

                                    //Invio del titolo
                                    dos.writeBytes(rs.getString("sottotitolo") + '\n');


                                    //Calcolo ed invio degli utenti online per quella sessione
                                    number_user_online_session = 0;
                                    rs_temp = stmt.executeQuery("SELECT * from Utenti u inner join R_Utente_Sessione rus on rus.id_utente = u.id WHERE u.stato_login = 1 AND rus.id_sessione = " + rs.getString("id"));
                                    while (rs_temp.next()) {
                                        number_user_online_session++;
                                    }
                                    dos.writeBytes(Integer.toString(number_user_online_session) + '\n');



                                }


                            }
	
	                    }
	                    catch(ClassNotFoundException e)
	                    {
	                        System.out.println(e);
	                    }
	                    catch(SQLException e)
	                    {
	                        System.out.println(e);
	                    }

                	}
                	else
                		dos.writeBytes("-1" + '\n');

                    

                }


                //s
                else if (received.equals("get_number_user_online_session"))
                {
                    if(is_disconnect == false && is_connect == true) {


                        dos.writeBytes(Integer.toString(number_sessions) + '\n');

                        if (number_sessions != 0) {
                            rs = stmt.executeQuery("SELECT * from Sessioni s inner join R_Utente_Sessione rus on rus.id_sessione = s.id where rus.id_utente = " + client_id);
                            while (rs.next()) {

                                //Calcolo ed invio degli utenti online per quella sessione
                                number_user_online_session = 0;
                                rs_temp = stmt.executeQuery("SELECT * from Utenti u inner join R_Utente_Sessione rus on rus.id_utente = u.id WHERE u.stato_login = 1 AND rus.id_sessione = " + rs.getString("id"));
                                while (rs_temp.next()) {
                                    number_user_online_session++;
                                }
                                dos.writeBytes(Integer.toString(number_user_online_session) + '\n');

                            }


                        }
                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }


                else if (received.equals("connect_session"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == false) {
                        id_passed_session = "";
                        id_passed_session = dis.readLine();

                        i = 0;
                        rs = stmt.executeQuery("SELECT * from Sessioni s inner join R_Utente_Sessione rus on rus.id_sessione = s.id where rus.id_utente = " + client_id);
                        while (rs.next()) {


                            session_id = rs.getInt("id");

                            if(i == Integer.parseInt(id_passed_session))
                            {
                                is_session_connected = true;
                                dos.writeBytes("1" + '\n');

                                if(rs.getInt("id_host") != client_id) //Se l'utente non è host allora mi fai vedere la vista dei characters
                                {
                                    is_host = false;
                                    rs_temp = stmt.executeQuery("SELECT * FROM Personaggi p WHERE p.id_sessione = " + session_id + " AND p.id_utente = " + client_id + " AND p.is_character = 1");
                                    while (rs_temp.next()) {
                                        number_characters++;
                                    }

                                    dos.writeBytes(Integer.toString(number_characters) + '\n'); //Ritorno il numero di characters per quell'utente

                                    rs_temp = stmt.executeQuery("SELECT * FROM Personaggi p WHERE p.id_sessione = " + session_id + " AND p.id_utente = " + client_id + " AND p.is_character = 1");
                                    while (rs_temp.next()) {
                                        dos.writeBytes(rs_temp.getString("nome") + '\n');
                                        dos.writeBytes(Integer.toString(rs_temp.getInt("hp")) + '\n');
                                        dos.writeBytes(Integer.toString(rs_temp.getInt("hp_max")) + '\n');
                                    }


                                }

                                else
                                {
                                    //FUNZIONI PER IL GAME MASTER
                                    is_host = true;

                                }

                            }

                            i++;

                        }

                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }

                else if (received.equals("disconnect_session"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == false) {
                        character_id = 0;
                        session_id = 0;
                        is_host = false;
                        is_character_connected = false;
                        is_session_connected = false;
                        dos.writeBytes("1" + '\n');

                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }


                else if (received.equals("get_users_session"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true) {

                        if(number_sessions != 0)
                        {
                            dos.writeBytes("1" + '\n');

                            //Calcolo ed invio degli utenti online per quella sessione
                            number_user_online_session = 0;
                            rs_temp = stmt.executeQuery("SELECT * from Utenti u inner join R_Utente_Sessione rus on rus.id_utente = u.id WHERE rus.id_sessione = " + rs.getString("id"));
                            while (rs_temp.next()) {
                                number_user_online_session++;
                            }
                            dos.writeBytes(Integer.toString(number_user_online_session) + '\n');

                            rs_temp = stmt.executeQuery("SELECT * from Utenti u inner join R_Utente_Sessione rus on rus.id_utente = u.id WHERE AND rus.id_sessione = " + rs.getString("id"));
                            while (rs_temp.next()) {

                                dos.writeBytes(rs_temp.getString("nome_utente") + '\n'); // Restituisco il nome dell'oggetto

                                if(rs_temp.getInt("stato_login") == 1)
                                {
                                    dos.writeBytes("is_logged" + '\n');
                                }
                                else if(rs_temp.getInt("stato_login") == 0)
                                {
                                    dos.writeBytes("is_not_logged" + '\n');
                                }

                                image = rs_temp.getBlob("imm_profilo");
                                byte barr[] = image.getBytes(1,(int)image.length());
                                dos.write(barr);

                            }
                        }

                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }


                else if (received.equals("show_user_session"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true) {

                        if(number_sessions != 0)
                        {
                            dos.writeBytes("1" + '\n');

                            rs = stmt.executeQuery("SELECT * from Sessioni s inner join R_Utente_Sessione rus on rus.id_sessione = s.id where rus.id_utente = " + client_id);
                            while (rs.next()) {

                                //Invio del titolo
                                dos.writeBytes(rs.getString("titolo") + '\n');

                                //Invio del titolo
                                dos.writeBytes(rs.getString("sottotitolo") + '\n');


                                //Calcolo ed invio degli utenti online per quella sessione
                                number_user_online_session = 0;
                                rs_temp = stmt.executeQuery("SELECT * from Utenti u inner join R_Utente_Sessione rus on rus.id_utente = u.id WHERE u.stato_login = 1 AND rus.id_sessione = " + rs.getString("id"));
                                while (rs_temp.next()) {
                                    number_user_online_session++;
                                }
                                dos.writeBytes(Integer.toString(number_user_online_session) + '\n');



                            }


                        }



                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }



                else if (received.equals("connect_character"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true) {
                        id_passed_character = "";
                        id_passed_character = dis.readLine();

                        i = 0;
                        rs = stmt.executeQuery("SELECT * FROM Personaggi p WHERE p.id_sessione = " + session_id + " AND p.id_utente = " + client_id + " AND p.is_character = 1");
                        while (rs.next()) {


                            character_id = rs.getInt("id");

                            if(i == Integer.parseInt(id_passed_character))
                            {
                                is_character_connected = true;
                                dos.writeBytes("1" + '\n');
                            }

                            i++;

                        }

                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }


                else if (received.equals("disconnect_character"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {
                        character_id = 0;
                        is_character_connected = false;

                        dos.writeBytes("1" + '\n');

                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }


                else if (received.equals("get_backpack"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {

                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        number_items_b = 0;

                        rs = stmt.executeQuery("SELECT * from R_Personaggio_Oggetto rpo WHERE rpo.id_personaggio = " + character_id);
                        while (rs.next()) {

                           number_items_b++;

                        }

                        dos.writeBytes(Integer.toString(number_items_b) + '\n'); // Restituisco il numero di oggetti del backpack

                        image = null;

                        rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN R_Personaggio_Oggetto rpo on rpo.id_oggetto = o.id WHERE rpo.id_personaggio = " + character_id);
                        while (rs.next()) {

                            dos.writeBytes(rs.getString("nome") + '\n'); // Restituisco il nome dell'oggetto
                            dos.writeBytes(rs.getString("descrizione") + '\n'); // Restituisco la descrizione dell'oggetto

                            image = rs.getBlob("foto");
                            byte barr[] = image.getBytes(1,(int)image.length());
                            dos.write(barr);

                            //dos.writeBytes( + '\n'); // Restituisco la foto dell'oggetto
                            dos.writeBytes(rs.getString("valore") + '\n'); // Restituisco il valore dell'oggetto

                            dos.writeBytes(rs.getString("quantita") + '\n'); // Restituisco la quantita dell'oggetto
                        }


                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }

                else if (received.equals("get_user_backpack")) // DA CAMBIARE
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {

                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        filter_item = dis.readLine(); //Mi prendo l'id dell'utente

                        send_id = Integer.parseInt(filter_item);

                        number_items_b = 0;

                        rs = stmt.executeQuery("SELECT * from R_Personaggio_Oggetto rpo WHERE rpo.id_personaggio = " + send_id);
                        while (rs.next()) {

                            number_items_b++;

                        }

                        dos.writeBytes(Integer.toString(number_items_b) + '\n'); // Restituisco il numero di oggetti del backpack

                        image = null;

                        rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN R_Personaggio_Oggetto rpo on rpo.id_oggetto = o.id WHERE rpo.id_personaggio = " + send_id);
                        while (rs.next()) {

                            dos.writeBytes(rs.getString("nome") + '\n'); // Restituisco il nome dell'oggetto
                            dos.writeBytes(rs.getString("descrizione") + '\n'); // Restituisco la descrizione dell'oggetto

                            image = rs.getBlob("foto");
                            byte barr[] = image.getBytes(1,(int)image.length());
                            dos.write(barr);

                            //dos.writeBytes( + '\n'); // Restituisco la foto dell'oggetto
                            dos.writeBytes(rs.getString("valore") + '\n'); // Restituisco il valore dell'oggetto

                            dos.writeBytes(rs.getString("quantita") + '\n'); // Restituisco la quantita dell'oggetto
                        }


                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }

                else if (received.equals("get_character_backpack"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {

                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        filter_item = dis.readLine(); //Mi prendo il nome del character

                        find_character = false;

                        rs = stmt.executeQuery("SELECT * from Personaggi p WHERE p.nome = " + filter_item + " AND p.is_character = 1 AND p.id_sessione = " + session_id);
                        while (rs.next()) {
                            find_character = true;
                            send_id = rs.getInt("id");
                        }


                        number_items_b = 0;

                        if(find_character == true)
                        {

                            rs = stmt.executeQuery("SELECT * from R_Personaggio_Oggetto rpo WHERE rpo.id_personaggio = " + send_id);
                            while (rs.next()) {

                                number_items_b++;

                            }

                            dos.writeBytes(Integer.toString(number_items_b) + '\n'); // Restituisco il numero di oggetti del backpack

                            image = null;

                            rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN R_Personaggio_Oggetto rpo on rpo.id_oggetto = o.id WHERE rpo.id_personaggio = " + send_id);
                            while (rs.next()) {

                                dos.writeBytes(rs.getString("nome") + '\n'); // Restituisco il nome dell'oggetto
                                dos.writeBytes(rs.getString("descrizione") + '\n'); // Restituisco la descrizione dell'oggetto

                                image = rs.getBlob("foto");
                                byte barr[] = image.getBytes(1, (int) image.length());
                                dos.write(barr);

                                //dos.writeBytes( + '\n'); // Restituisco la foto dell'oggetto
                                dos.writeBytes(rs.getString("valore") + '\n'); // Restituisco il valore dell'oggetto

                                dos.writeBytes(rs.getString("quantita") + '\n'); // Restituisco la quantita dell'oggetto
                            }
                        }
                        else
                            dos.writeBytes("-2" + '\n');

                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }


                else if (received.equals("get_equipment"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {

                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        number_items_b = 0;

                        rs = stmt.executeQuery("SELECT * from R_Personaggio_Oggetto rpo WHERE rpo.id_personaggio = " + character_id + " AND (is_head_eq = 1 OR is_torso_eq = 1 OR is_left_arm_eq = 1 OR is_right_arm_eq = 1 OR is_left_leg_eq = 1 OR is_right_leg_eq = 1 OR is_first_weapon_eq = 1 OR is_second_weapon_eq = 1 OR is_gloves_eq = 1 OR is_left_gloves_eq = 1 OR is_right_gloves_eq = 1 OR is_shoes_eq = 1 OR is_greaves_eq = 1 OR is_left_greaves_eq = 1 OR is_right_greaves_eq = 1)");
                        while (rs.next()) {

                            number_items_b++;

                        }

                        dos.writeBytes(Integer.toString(number_items_b) + '\n'); // Restituisco il numero di oggetti del backpack

                        image = null;

                        rs = stmt.executeQuery("SELECT * from R_Personaggio_Oggetto rpo WHERE rpo.id_personaggio = " + character_id + " AND (is_head_eq = 1 OR is_torso_eq = 1 OR is_left_arm_eq = 1 OR is_right_arm_eq = 1 OR is_left_leg_eq = 1 OR is_right_leg_eq = 1 OR is_first_weapon_eq = 1 OR is_second_weapon_eq = 1 OR is_gloves_eq = 1 OR is_left_gloves_eq = 1 OR is_right_gloves_eq = 1 OR is_shoes_eq = 1 OR is_greaves_eq = 1 OR is_left_greaves_eq = 1 OR is_right_greaves_eq = 1)");
                        while (rs.next()) {

                            if(rs.getInt("is_head_eq") == 1)
                            {
                                dos.writeBytes("head" + '\n');

                            }
                            else if (rs.getInt("is_torso_eq") == 1)
                            {
                                dos.writeBytes("torso" + '\n');

                            }

                            else if (rs.getInt("is_left_arm_eq") == 1)
                            {
                                dos.writeBytes("left_arm" + '\n');

                            }

                            else if (rs.getInt("is_right_arm_eq") == 1)
                            {
                                dos.writeBytes("right_arm" + '\n');

                            }

                            else if (rs.getInt("is_left_leg_eq") == 1)
                            {
                                dos.writeBytes("left_leg" + '\n');

                            }

                            else if (rs.getInt("is_right_leg_eq") == 1)
                            {
                                dos.writeBytes("right_leg" + '\n');

                            }

                            else if (rs.getInt("is_first_weapon_eq") == 1)
                            {
                                dos.writeBytes("first_weapon" + '\n');

                            }

                            else if (rs.getInt("is_secondary_weapon_eq") == 1)
                            {
                                dos.writeBytes("secondary_weapon" + '\n');

                            }

                            else if (rs.getInt("is_gloves_eq") == 1)
                            {
                                dos.writeBytes("gloves" + '\n');

                            }

                            else if (rs.getInt("is_left_gloves_eq") == 1)
                            {
                                dos.writeBytes("left_gloves" + '\n');

                            }

                            else if (rs.getInt("is_right_gloves_eq") == 1)
                            {
                                dos.writeBytes("right_gloves" + '\n');

                            }

                            else if (rs.getInt("is_shoes_eq") == 1)
                            {
                                dos.writeBytes("shoes" + '\n');

                            }

                            else if (rs.getInt("is_greaves_eq") == 1)
                            {
                                dos.writeBytes("greaves" + '\n');

                            }

                            else if (rs.getInt("is_left_greaves_eq") == 1)
                            {
                                dos.writeBytes("left_greaves" + '\n');

                            }

                            else if (rs.getInt("is_right_greaves_eq") == 1)
                            {
                                dos.writeBytes("right_greaves" + '\n');

                            }

                            dos.writeBytes(rs.getString("nome") + '\n'); // Restituisco il nome dell'oggetto
                            dos.writeBytes(rs.getString("descrizione") + '\n'); // Restituisco la descrizione dell'oggetto

                            image = rs.getBlob("foto");
                            byte barr[] = image.getBytes(1,(int)image.length());
                            dos.write(barr);

                            //dos.writeBytes( + '\n'); // Restituisco la foto dell'oggetto
                            dos.writeBytes(rs.getString("valore") + '\n'); // Restituisco il valore dell'oggetto

                        }


                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }

                else if (received.equals("get_equipment_from_category"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {

                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        number_items_b = 0;

                        c_name = dis.readLine();


                        if(c_name.equals("head"))
                        {
                            ob_type = "is_head_eq";
                        }
                        else if (c_name.equals("torso"))
                        {
                            ob_type = "is_torso_eq";
                        }

                        else if (c_name.equals("left_arm"))
                        {
                            ob_type = "is_left_arm_eq";
                        }

                        else if (c_name.equals("right_arm"))
                        {
                            ob_type = "is_right_arm_eq";
                        }

                        else if (c_name.equals("left_leg"))
                        {
                            ob_type = "is_left_leg_eq";
                        }

                        else if (c_name.equals("right_leg"))
                        {
                            ob_type = "is_right_leg_eq";
                        }

                        else if (c_name.equals("first_weapon"))
                        {
                            ob_type = "is_first_weapon_eq";
                        }

                        else if (c_name.equals("secondary_weapon"))
                        {
                            ob_type = "is_secondary_weapon_eq";
                        }

                        else if (c_name.equals("gloves"))
                        {
                            ob_type = "is_gloves_eq";
                        }

                        else if (c_name.equals("left_gloves"))
                        {
                            ob_type = "is_left_gloves_eq";
                        }

                        else if (c_name.equals("right_gloves"))
                        {
                            ob_type = "is_right_gloves_eq";
                        }

                        else if (c_name.equals("shoes"))
                        {
                            ob_type = "is_shoes_eq";
                        }

                        else if (c_name.equals("greaves"))
                        {
                            ob_type = "is_greaves_eq";
                        }

                        else if (c_name.equals("left_greaves"))
                        {
                            ob_type = "is_left_greaves_eq";
                        }

                        else if (c_name.equals("right_greaves"))
                        {
                            ob_type = "is_right_greaves_eq";
                        }
                        else
                        {
                            ob_type = null;
                        }


                        rs = stmt.executeQuery("SELECT * from R_Personaggio_Oggetto rpo WHERE rpo.id_personaggio = " + character_id + " AND " + ob_type + " = 1");
                        while (rs.next()) {

                            number_items_b++;

                        }

                        dos.writeBytes(Integer.toString(number_items_b) + '\n'); // Restituisco il numero di oggetti del backpack

                        image = null;

                        rs = stmt.executeQuery("SELECT * from R_Personaggio_Oggetto rpo WHERE rpo.id_personaggio = " + character_id + " AND " + ob_type + " = 1");
                        while (rs.next()) {



                            dos.writeBytes(rs.getString("nome") + '\n'); // Restituisco il nome dell'oggetto
                            dos.writeBytes(rs.getString("descrizione") + '\n'); // Restituisco la descrizione dell'oggetto

                            image = rs.getBlob("foto");
                            byte barr[] = image.getBytes(1,(int)image.length());
                            dos.write(barr);

                            //dos.writeBytes( + '\n'); // Restituisco la foto dell'oggetto
                            dos.writeBytes(rs.getString("valore") + '\n'); // Restituisco il valore dell'oggetto

                        }


                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }


                else if (received.equals("get_filtered_backpack"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {

                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        number_items_b = 0;

                        filter_item = dis.readLine(); //Mi prendo il filtro

                        rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN R_Personaggio_Oggetto rpo on rpo.id_oggetto = o.id INNER JOIN Categorie c ON c.id = o.id_categoria WHERE rpo.id_personaggio = " + character_id + " AND c.nome = " + filter_item);
                        while (rs.next()) {

                            number_items_b++;

                        }

                        dos.writeBytes(Integer.toString(number_items_b) + '\n'); // Restituisco il numero di oggetti del backpack

                        image = null;

                        rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN R_Personaggio_Oggetto rpo on rpo.id_oggetto = o.id INNER JOIN Categorie c ON c.id = o.id_categoria WHERE rpo.id_personaggio = " + character_id + " AND c.nome = " + filter_item);
                        while (rs.next()) {

                            dos.writeBytes(rs.getString("nome") + '\n'); // Restituisco il nome dell'oggetto
                            dos.writeBytes(rs.getString("descrizione") + '\n'); // Restituisco la descrizione dell'oggetto

                            image = rs.getBlob("foto");
                            byte barr[] = image.getBytes(1,(int)image.length());
                            dos.write(barr);

                            //dos.writeBytes( + '\n'); // Restituisco la foto dell'oggetto
                            dos.writeBytes(rs.getString("valore") + '\n'); // Restituisco il valore dell'oggetto


                        }


                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }


                else if (received.equals("create_category"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true) {


                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;

                        c_name = dis.readLine(); //Mi prendo il nome della categoria
                        c_desc = dis.readLine(); //Mi prendo la descrizione della categoria


                        rs = stmt.executeQuery("SELECT * from Categorie c WHERE c.id_sessione = " + session_id);
                        while (rs.next()) {

                            if(c_name.equals(rs.getString("nome")))
                                find_category = true;

                        }

                        if(find_category)
                        {
                            dos.writeBytes("-2" + '\n');
                        }

                        else
                        {
                            pstmt = conn.prepareStatement("INSERT INTO Category " + "(nome, descrizione, id_sessione) values (?,?,?)");


                            pstmt.setString(1, c_name);
                            pstmt.setString(2, c_desc);
                            pstmt.setInt(3, session_id);
                            pstmt.execute();
                            pstmt.close(); // rilascio le risorse

                            dos.writeBytes("1" + '\n');
                        }

                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }


                else if (received.equals("show_category"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true) {


                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        number_categories = 0;

                        rs = stmt.executeQuery("SELECT * from Categorie c WHERE c.id_sessione = " + session_id);
                        while (rs.next()) {

                            number_categories++;

                        }

                        dos.writeBytes(Integer.toString(number_categories) + '\n'); //Ritorno il numero di characters per quell'utente


                        image = null;

                        rs = stmt.executeQuery("SELECT * from Categorie c WHERE c.id_sessione = " + session_id);
                        while (rs.next()) {
                            dos.writeBytes(rs_temp.getString("nome") + '\n');
                            dos.writeBytes(rs_temp.getString("descrizione") + '\n');

                            image = rs.getBlob("foto");
                            byte barr[] = image.getBytes(1,(int)image.length());
                            dos.write(barr);

                            number_object_categories = 0;

                            rs_temp = stmt.executeQuery("SELECT * from Oggetti o WHERE o.id_categoria = " + rs.getString("id") + " AND o.id_sessione = " + session_id);
                            while (rs_temp.next()) {
                                number_object_categories++;
                            }
                            dos.writeBytes(Integer.toString(number_object_categories) + '\n'); //Ritorno il numero di oggetti per categoria

                        }



                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }

                else if (received.equals("show_category_objects"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true) {

                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        c_name = dis.readLine();

                        find_category = false;

                        rs = stmt.executeQuery("SELECT * from Category c WHERE c.nome = " + c_name + " AND c.id_sessione = " + session_id);
                        while (rs.next()) {
                            find_category = true;
                            category_id = rs.getInt("id");
                        }


                        if(find_category == false)
                        {
                            dos.writeBytes("-2" + '\n');
                        }
                        else
                        {
                            rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN Categorie c ON o.id_categoria = c.id  WHERE c.id = " + category_id);
                            while (rs.next()) {
                                number_object_categories++;
                            }


                            dos.writeBytes(Integer.toString(number_object_categories) + '\n'); // Restituisco il numero di oggetti del backpack

                            image = null;

                            rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN Categorie c ON o.id_categoria = c.id  WHERE c.id = " + category_id);
                            while (rs.next()) {

                                dos.writeBytes(rs.getString("nome") + '\n'); // Restituisco il nome dell'oggetto
                                dos.writeBytes(rs.getString("descrizione") + '\n'); // Restituisco la descrizione dell'oggetto

                                image = rs.getBlob("foto");
                                byte barr[] = image.getBytes(1,(int)image.length());
                                dos.write(barr);

                                //dos.writeBytes( + '\n'); // Restituisco la foto dell'oggetto
                                dos.writeBytes(rs.getString("valore") + '\n'); // Restituisco il valore dell'oggetto

                                dos.writeBytes(rs.getString("quantita") + '\n'); // Restituisco la quantita dell'oggetto
                            }

                        }

                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }


                else if (received.equals("show_user_character"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == false) {

                        dos.writeBytes("1" + '\n');

                        is_host = false;
                        rs_temp = stmt.executeQuery("SELECT * FROM Personaggi p WHERE p.id_sessione = " + session_id + " AND p.id_utente = " + client_id + " AND p.is_character = 1");
                        while (rs_temp.next()) {
                            number_characters++;
                        }

                        dos.writeBytes(Integer.toString(number_characters) + '\n'); //Ritorno il numero di characters per quell'utente

                        rs_temp = stmt.executeQuery("SELECT * FROM Personaggi p WHERE p.id_sessione = " + session_id + " AND p.id_utente = " + client_id + " AND p.is_character = 1");
                        while (rs_temp.next()) {
                            dos.writeBytes(rs_temp.getString("nome") + '\n');
                            dos.writeBytes(Integer.toString(rs_temp.getInt("hp")) + '\n');
                            dos.writeBytes(Integer.toString(rs_temp.getInt("hp_max")) + '\n');
                        }





                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }

                //TRADING
                else if (received.equals("trading_with"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {

                        is_trading_enabled1 = false;
                        is_trading_enabled2 = false;

                        dos.writeBytes("1" + '\n');

                        trade_name_c = dis.readLine();

                        find_trade = false;

                        rs_temp = stmt.executeQuery("SELECT * FROM Personaggi p WHERE p.id_sessione = " + session_id + " AND p.nome = " + character_id + " AND p.is_character = 1");
                        while (rs_temp.next()) {

                            if(rs.getInt("is_trading_enabled") == 0)
                            {
                                is_trading_enabled1 = false;
                            }
                            else
                            {
                                is_trading_enabled1 = true;
                            }
                        }


                        rs_temp = stmt.executeQuery("SELECT * FROM Personaggi p WHERE p.id_sessione = " + session_id + " AND p.nome = " + trade_name_c + " AND p.is_character = 1");
                        while (rs_temp.next()) {
                            find_trade = true;
                            send_id = rs.getInt("id");

                            if(rs.getInt("is_trading_enabled") == 0)
                            {
                                is_trading_enabled2 = false;
                            }
                            else
                            {
                                is_trading_enabled2 = true;
                            }
                        }

                        if(find_trade && is_trading_enabled2 && is_trading_enabled1)
                        {
                            dos.writeBytes("1" + '\n');

                            number_item_trade_1 = Integer.parseInt(dis.readLine());

                            number_item_trade_2 = Integer.parseInt(dis.readLine());

                            for(i = 0; i < number_item_trade_1; i++)
                            {
                                trade_q_1[i] = Integer.parseInt(dis.readLine());
                                trade_o_1[i] = dis.readLine();
                            }

                            for(i = 0; i < number_item_trade_2; i++)
                            {
                                trade_q_2[i] = Integer.parseInt(dis.readLine());
                                trade_o_2[i] = dis.readLine();
                            }


                            is_o1_ok = true;
                            is_o2_ok = true;

                            for(i = 0; i < number_item_trade_1; i++)
                            {
                                find_trade = false;
                                rs_temp = stmt.executeQuery("SELECT * FROM R_Personaggio_Oggetto rpo INNER JOIN Oggetti o on rpo.id_oggetto = o.id WHERE rpo.id_personaggio = " + character_id + " AND o.nome = " + trade_o_1[i] + " AND rpo.quantita <= " + trade_q_1[i]);
                                while (rs_temp.next()) {
                                    find_trade = true;
                                    id_items_1[i] = rs_temp.getInt("id");
                                    quantity_items_1[i] = rs_temp.getInt("quantita");
                                }

                                if(find_trade == false)
                                {
                                    is_o1_ok = false;
                                    break;
                                }
                            }

                            for(i = 0; i < number_item_trade_2; i++)
                            {
                                find_trade = false;
                                rs_temp = stmt.executeQuery("SELECT * FROM R_Personaggio_Oggetto rpo INNER JOIN Oggetti o on rpo.id_oggetto = o.id WHERE rpo.id_personaggio = " + send_id + " AND o.nome = " + trade_o_2[i] + " AND rpo.quantita <= " + trade_q_2[i]);
                                while (rs_temp.next()) {
                                    find_trade = true;
                                    id_items_2[i] = rs_temp.getInt("id");
                                    //Salvati i dati della quantità
                                    quantity_items_2[i] = rs_temp.getInt("quantita");
                                }

                                if(find_trade == false)
                                {
                                    is_o2_ok = false;
                                    break;
                                }
                            }


                            is_null_1 = false;

                            if(is_o1_ok == true && is_o2_ok == true)
                            {

                                //Faccio l'update del primo utente

                                for(i = 0; i < number_item_trade_1; i++)
                                {
                                    if(quantity_items_1[i] - trade_q_1[i] == 0)
                                    {
                                        pstmt = conn.prepareStatement("DELETE FROM R_Personaggio_Oggetto WHERE id_personaggio = ? AND id_oggetto = ?");
                                        pstmt.setInt(1, character_id);
                                        pstmt.setInt(2, id_items_1[i]);
                                        pstmt.execute();
                                        pstmt.close(); // rilascio le risorse
                                    }

                                    else if (quantity_items_1[i] - trade_q_1[i] > 0)
                                    {
                                        x = quantity_items_1[i] - trade_q_1[i];
                                        pstmt = conn.prepareStatement("UPDATE FROM R_Personaggio_Oggetto SET quantita = " + x + " WHERE id_personaggio = " + character_id + " AND id_oggetto = " + id_items_1[i]);
                                        //pstmt.setInt(1, character_id);
                                        //pstmt.setInt(2, id_items_1[i]);
                                        pstmt.execute();
                                        pstmt.close(); // rilascio le risorse
                                    }

                                    else
                                        is_null_1 = true;

                                }


                                //Faccio l'update del secondo utente

                                for(i = 0; i < number_item_trade_2; i++)
                                {
                                    if(quantity_items_2[i] - trade_q_2[i] == 0)
                                    {
                                        pstmt = conn.prepareStatement("DELETE FROM R_Personaggio_Oggetto WHERE id_personaggio = ? AND id_oggetto = ?");
                                        pstmt.setInt(1, send_id);
                                        pstmt.setInt(2, id_items_2[i]);
                                        pstmt.execute();
                                        pstmt.close(); // rilascio le risorse
                                    }

                                    else if (quantity_items_2[i] - trade_q_2[i] > 0)
                                    {
                                        x = quantity_items_2[i] - trade_q_2[i];
                                        pstmt = conn.prepareStatement("UPDATE FROM R_Personaggio_Oggetto SET quantita = " + x + " WHERE id_personaggio = " + send_id + " AND id_oggetto = " + id_items_2[i]);
                                        //pstmt.setInt(1, character_id);
                                        //pstmt.setInt(2, id_items_1[i]);
                                        pstmt.execute();
                                        pstmt.close(); // rilascio le risorse
                                    }

                                    else
                                        is_null_2 = true;

                                }


                                if(is_null_1 == false && is_null_2 == false)
                                {
                                    //Adesso aggiungo gli oggetti del trading del secondo giocatore al primo giocatore
                                    for(i = 0; i < number_item_trade_2; i++)
                                    {
                                        find_trade = false;
                                        rs_temp = stmt.executeQuery("SELECT * FROM R_Personaggio_Oggetto rpo WHERE rpo.id_personaggio = " + character_id + " AND rpo.id_oggetto = " + id_items_2[i]);
                                        while (rs_temp.next()) {
                                            find_trade = true;
                                            x = rs_temp.getInt("quantita");
                                        }

                                        if(find_trade == true)
                                        {
                                            x = x + quantity_items_2[i];

                                            pstmt = conn.prepareStatement("UPDATE FROM R_Personaggio_Oggetto SET quantita = " + x + " WHERE id_personaggio = " + character_id + " AND id_oggetto = " + id_items_2[i]);
                                            //pstmt.setInt(1, character_id);
                                            //pstmt.setInt(2, id_items_1[i]);
                                            pstmt.execute();
                                            pstmt.close(); // rilascio le risorse
                                        }
                                        else
                                        {
                                            pstmt = conn.prepareStatement("INSERT INTO R_Personaggio_Oggetto " + "(id_personaggio, id_oggetto, quantita) values (?,?,?)");
                                            pstmt.setInt(1, character_id);
                                            pstmt.setInt(2, id_items_2[i]);
                                            pstmt.setInt(3, trade_q_2[i]);
                                            pstmt.execute();
                                            pstmt.close(); // rilascio le risorse
                                        }



                                    }


                                    //Adesso aggiungo gli oggetti del trading del primo giocatore al secondo giocatore
                                    for(i = 0; i < number_item_trade_1; i++)
                                    {
                                        find_trade = false;
                                        rs_temp = stmt.executeQuery("SELECT * FROM R_Personaggio_Oggetto rpo WHERE rpo.id_personaggio = " + send_id + " AND rpo.id_oggetto = " + id_items_1[i]);
                                        while (rs_temp.next()) {
                                            find_trade = true;
                                            x = rs_temp.getInt("quantita");
                                        }

                                        if(find_trade == true)
                                        {
                                            x = x + quantity_items_2[i];

                                            pstmt = conn.prepareStatement("UPDATE FROM R_Personaggio_Oggetto SET quantita = " + x + " WHERE id_personaggio = " + send_id + " AND id_oggetto = " + id_items_1[i]);
                                            //pstmt.setInt(1, character_id);
                                            //pstmt.setInt(2, id_items_1[i]);
                                            pstmt.execute();
                                            pstmt.close(); // rilascio le risorse
                                        }
                                        else
                                        {
                                            pstmt = conn.prepareStatement("INSERT INTO R_Personaggio_Oggetto " + "(id_personaggio, id_oggetto, quantita) values (?,?,?)");
                                            pstmt.setInt(1, send_id);
                                            pstmt.setInt(2, id_items_1[i]);
                                            pstmt.setInt(3, trade_q_1[i]);
                                            pstmt.execute();
                                            pstmt.close(); // rilascio le risorse
                                        }


                                        dos.writeBytes("1" + '\n');


                                    }
                                }
                                else
                                {
                                    dos.writeBytes("-4" + '\n');
                                }


                            }



                            else
                            {
                                dos.writeBytes("-3" + '\n');
                            }

                        }
                        else
                        {
                            dos.writeBytes("-2" + '\n');
                        }


                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }


                //TRADING FROM
                else if (received.equals("trading_from"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {

                        is_trading_enabled1 = false;
                        is_trading_enabled2 = false;

                        dos.writeBytes("1" + '\n');

                        trade_c_name1 = dis.readLine();
                        trade_c_name2 = dis.readLine();

                        find_trade1 = false;
                        find_trade2 = false;

                        rs_temp = stmt.executeQuery("SELECT * FROM Personaggi p WHERE p.id_sessione = " + session_id + " AND p.nome = " + trade_c_name1 + " AND p.is_character = 1");
                        while (rs_temp.next()) {

                            find_trade1 = true;


                            if(rs.getInt("is_trading_enabled") == 0)
                            {
                                is_trading_enabled1 = false;
                            }
                            else
                            {
                                is_trading_enabled1 = true;
                            }
                        }


                        rs_temp = stmt.executeQuery("SELECT * FROM Personaggi p WHERE p.id_sessione = " + session_id + " AND p.nome = " + trade_c_name2 + " AND p.is_character = 1");
                        while (rs_temp.next()) {
                            find_trade2 = true;
                            send_id = rs.getInt("id");

                            if(rs.getInt("is_trading_enabled") == 0)
                            {
                                is_trading_enabled2 = false;
                            }
                            else
                            {
                                is_trading_enabled2 = true;
                            }
                        }

                        if(find_trade1 && find_trade2 && is_trading_enabled2 && is_trading_enabled1)
                        {
                            dos.writeBytes("1" + '\n');

                            number_item_trade_1 = Integer.parseInt(dis.readLine());

                            number_item_trade_2 = Integer.parseInt(dis.readLine());

                            for(i = 0; i < number_item_trade_1; i++)
                            {
                                trade_q_1[i] = Integer.parseInt(dis.readLine());
                                trade_o_1[i] = dis.readLine();
                            }

                            for(i = 0; i < number_item_trade_2; i++)
                            {
                                trade_q_2[i] = Integer.parseInt(dis.readLine());
                                trade_o_2[i] = dis.readLine();
                            }


                            is_o1_ok = true;
                            is_o2_ok = true;

                            for(i = 0; i < number_item_trade_1; i++)
                            {
                                find_trade = false;
                                rs_temp = stmt.executeQuery("SELECT * FROM R_Personaggio_Oggetto rpo INNER JOIN Oggetti o on rpo.id_oggetto = o.id WHERE rpo.id_personaggio = " + trade_c_id1 + " AND o.nome = " + trade_o_1[i] + " AND rpo.quantita <= " + trade_q_1[i]);
                                while (rs_temp.next()) {
                                    find_trade = true;
                                    id_items_1[i] = rs_temp.getInt("id");
                                    quantity_items_1[i] = rs_temp.getInt("quantita");
                                }

                                if(find_trade == false)
                                {
                                    is_o1_ok = false;
                                    break;
                                }
                            }

                            for(i = 0; i < number_item_trade_2; i++)
                            {
                                find_trade = false;
                                rs_temp = stmt.executeQuery("SELECT * FROM R_Personaggio_Oggetto rpo INNER JOIN Oggetti o on rpo.id_oggetto = o.id WHERE rpo.id_personaggio = " + trade_c_id2 + " AND o.nome = " + trade_o_2[i] + " AND rpo.quantita <= " + trade_q_2[i]);
                                while (rs_temp.next()) {
                                    find_trade = true;
                                    id_items_2[i] = rs_temp.getInt("id");
                                    //Salvati i dati della quantità
                                    quantity_items_2[i] = rs_temp.getInt("quantita");
                                }

                                if(find_trade == false)
                                {
                                    is_o2_ok = false;
                                    break;
                                }
                            }


                            is_null_1 = false;

                            if(is_o1_ok == true && is_o2_ok == true)
                            {

                                //Faccio l'update del primo utente

                                for(i = 0; i < number_item_trade_1; i++)
                                {
                                    if(quantity_items_1[i] - trade_q_1[i] == 0)
                                    {
                                        pstmt = conn.prepareStatement("DELETE FROM R_Personaggio_Oggetto WHERE id_personaggio = ? AND id_oggetto = ?");
                                        pstmt.setInt(1, trade_c_id1);
                                        pstmt.setInt(2, id_items_1[i]);
                                        pstmt.execute();
                                        pstmt.close(); // rilascio le risorse
                                    }

                                    else if (quantity_items_1[i] - trade_q_1[i] > 0)
                                    {
                                        x = quantity_items_1[i] - trade_q_1[i];
                                        pstmt = conn.prepareStatement("UPDATE FROM R_Personaggio_Oggetto SET quantita = " + x + " WHERE id_personaggio = " + trade_c_id1 + " AND id_oggetto = " + id_items_1[i]);
                                        //pstmt.setInt(1, character_id);
                                        //pstmt.setInt(2, id_items_1[i]);
                                        pstmt.execute();
                                        pstmt.close(); // rilascio le risorse
                                    }

                                    else
                                        is_null_1 = true;

                                }


                                //Faccio l'update del secondo utente

                                for(i = 0; i < number_item_trade_2; i++)
                                {
                                    if(quantity_items_2[i] - trade_q_2[i] == 0)
                                    {
                                        pstmt = conn.prepareStatement("DELETE FROM R_Personaggio_Oggetto WHERE id_personaggio = ? AND id_oggetto = ?");
                                        pstmt.setInt(1, trade_c_id2);
                                        pstmt.setInt(2, id_items_2[i]);
                                        pstmt.execute();
                                        pstmt.close(); // rilascio le risorse
                                    }

                                    else if (quantity_items_2[i] - trade_q_2[i] > 0)
                                    {
                                        x = quantity_items_2[i] - trade_q_2[i];
                                        pstmt = conn.prepareStatement("UPDATE FROM R_Personaggio_Oggetto SET quantita = " + x + " WHERE id_personaggio = " + trade_c_id2 + " AND id_oggetto = " + id_items_2[i]);
                                        //pstmt.setInt(1, character_id);
                                        //pstmt.setInt(2, id_items_1[i]);
                                        pstmt.execute();
                                        pstmt.close(); // rilascio le risorse
                                    }

                                    else
                                        is_null_2 = true;

                                }


                                if(is_null_1 == false && is_null_2 == false)
                                {
                                    //Adesso aggiungo gli oggetti del trading del secondo giocatore al primo giocatore
                                    for(i = 0; i < number_item_trade_2; i++)
                                    {
                                        find_trade = false;
                                        rs_temp = stmt.executeQuery("SELECT * FROM R_Personaggio_Oggetto rpo WHERE rpo.id_personaggio = " + trade_c_id2 + " AND rpo.id_oggetto = " + id_items_2[i]);
                                        while (rs_temp.next()) {
                                            find_trade = true;
                                            x = rs_temp.getInt("quantita");
                                        }

                                        if(find_trade == true)
                                        {
                                            x = x + quantity_items_2[i];

                                            pstmt = conn.prepareStatement("UPDATE FROM R_Personaggio_Oggetto SET quantita = " + x + " WHERE id_personaggio = " + trade_c_id1 + " AND id_oggetto = " + id_items_2[i]);
                                            //pstmt.setInt(1, character_id);
                                            //pstmt.setInt(2, id_items_1[i]);
                                            pstmt.execute();
                                            pstmt.close(); // rilascio le risorse
                                        }
                                        else
                                        {
                                            pstmt = conn.prepareStatement("INSERT INTO R_Personaggio_Oggetto " + "(id_personaggio, id_oggetto, quantita) values (?,?,?)");
                                            pstmt.setInt(1, trade_c_id1);
                                            pstmt.setInt(2, id_items_2[i]);
                                            pstmt.setInt(3, trade_q_2[i]);
                                            pstmt.execute();
                                            pstmt.close(); // rilascio le risorse
                                        }



                                    }


                                    //Adesso aggiungo gli oggetti del trading del primo giocatore al secondo giocatore
                                    for(i = 0; i < number_item_trade_1; i++)
                                    {
                                        find_trade = false;
                                        rs_temp = stmt.executeQuery("SELECT * FROM R_Personaggio_Oggetto rpo WHERE rpo.id_personaggio = " + trade_c_id2 + " AND rpo.id_oggetto = " + id_items_1[i]);
                                        while (rs_temp.next()) {
                                            find_trade = true;
                                            x = rs_temp.getInt("quantita");
                                        }

                                        if(find_trade == true)
                                        {
                                            x = x + quantity_items_2[i];

                                            pstmt = conn.prepareStatement("UPDATE FROM R_Personaggio_Oggetto SET quantita = " + x + " WHERE id_personaggio = " + trade_c_id2 + " AND id_oggetto = " + id_items_1[i]);
                                            //pstmt.setInt(1, character_id);
                                            //pstmt.setInt(2, id_items_1[i]);
                                            pstmt.execute();
                                            pstmt.close(); // rilascio le risorse
                                        }
                                        else
                                        {
                                            pstmt = conn.prepareStatement("INSERT INTO R_Personaggio_Oggetto " + "(id_personaggio, id_oggetto, quantita) values (?,?,?)");
                                            pstmt.setInt(1, trade_c_id2);
                                            pstmt.setInt(2, id_items_1[i]);
                                            pstmt.setInt(3, trade_q_1[i]);
                                            pstmt.execute();
                                            pstmt.close(); // rilascio le risorse
                                        }


                                        dos.writeBytes("1" + '\n');


                                    }
                                }
                                else
                                {
                                    dos.writeBytes("-4" + '\n');
                                }


                            }



                            else
                            {
                                dos.writeBytes("-3" + '\n');
                            }

                        }
                        else
                        {
                            dos.writeBytes("-2" + '\n');
                        }


                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }

                //Get quantity of object per character
                //restituisce 1 perchè è entrato poi 1 se ha trovato l'oggetto sul character e lo restituisce poi -2 se non ha trovato l'oggetto e -1 se c'è errore
                else if (received.equals("get_quantity_object"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {

                        dos.writeBytes("1" + '\n');

                        trade_name_c = dis.readLine();

                        find_trade = false;
                        rs_temp = stmt.executeQuery("SELECT * FROM R_Personaggio_Oggetto rpo INNER JOIN Oggetti o on rpo.id_oggetto = o.id WHERE rpo.id_personaggio = " + character_id + " AND o.nome = " + trade_name_c);
                        while (rs_temp.next()) {
                            find_trade = true;
                            quantity_items_1[0] = rs_temp.getInt("quantita");
                        }

                        if(find_trade)
                        {
                            dos.writeBytes("1" + '\n');

                            dos.writeBytes(Integer.toString(quantity_items_1[0]));
                        }
                        else
                        {
                            dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }



                //Get quantity of object per un determinato character
                //restituisce 1 perchè è entrato poi 1 se ha trovato l'oggetto sul character e lo restituisce poi -2 se non ha trovato l'oggetto e -1 se c'è errore
                else if (received.equals("get_quantity_object_from_character"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true) {

                        dos.writeBytes("1" + '\n');

                        c_name = dis.readLine();
                        trade_name_c = dis.readLine();


                        find_trade = false;
                        rs_temp = stmt.executeQuery("SELECT * FROM R_Personaggio_Oggetto rpo INNER JOIN Oggetti o on rpo.id_oggetto = o.id INNER JOIN Personaggi p on p.id = rpo.id_personaggio WHERE p.nome = " + c_name + " AND o.nome = " + trade_name_c);
                        while (rs_temp.next()) {
                            find_trade = true;
                            quantity_items_1[0] = rs_temp.getInt("quantita");
                        }

                        if(find_trade)
                        {
                            dos.writeBytes("1" + '\n');

                            dos.writeBytes(Integer.toString(quantity_items_1[0]));
                        }
                        else
                        {
                            dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }



                //Creating new character
                else if (received.equals("add_character"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true) {


                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;

                        character_nome = dis.readLine();
                        character_hp = Integer.parseInt(dis.readLine());
                        character_biografia = dis.readLine();
                        character_razza = dis.readLine();
                        character_eta = Integer.parseInt(dis.readLine());
                        character_peso = Float.parseFloat(dis.readLine());
                        character_altezza = Float.parseFloat(dis.readLine());
                        character_hp_max = Integer.parseInt(dis.readLine());


                        rs = stmt.executeQuery("SELECT * from Personaggi p WHERE p.is_character = 1 AND p.id_sessione = " + session_id);
                        while (rs.next()) {

                            if(character_nome.equals(rs.getString("nome")))
                                find_category = true;

                        }

                        if(find_category)
                        {
                            dos.writeBytes("-2" + '\n');
                        }

                        else
                        {
                            pstmt = conn.prepareStatement("INSERT INTO Personaggi " + "(nome, hp, biografia, razza, eta, peso, altezza, hp_max, id_sessione, id_utente, is_character /*Immagine*/ ) values (?,?,?,?,?,?,?,?,?,?,?)");


                            pstmt.setString(1, c_name);
                            pstmt.setInt(2, character_hp);
                            pstmt.setString(3, character_biografia);
                            pstmt.setString(4, character_razza);
                            pstmt.setInt(5, character_eta);
                            pstmt.setFloat(6, character_peso);
                            pstmt.setFloat(7, character_altezza);
                            pstmt.setInt(8, character_hp_max);
                            pstmt.setInt(9, session_id);
                            pstmt.setInt(10, client_id);
                            pstmt.setInt(11, 1);

                            //Inserimento foto

                            pstmt.execute();
                            pstmt.close(); // rilascio le risorse

                            dos.writeBytes("1" + '\n');
                        }

                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }

                //Creating new personaggio
                else if (received.equals("add_personaggio"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true) {


                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;

                        character_nome = dis.readLine();
                        character_hp = Integer.parseInt(dis.readLine());
                        character_biografia = dis.readLine();
                        character_razza = dis.readLine();
                        character_eta = Integer.parseInt(dis.readLine());
                        character_peso = Float.parseFloat(dis.readLine());
                        character_altezza = Float.parseFloat(dis.readLine());
                        character_hp_max = Integer.parseInt(dis.readLine());
                        what_character = dis.readLine();

                        rs = stmt.executeQuery("SELECT * from Personaggi p WHERE p.id_sessione = " + session_id);
                        while (rs.next()) {

                            if(character_nome.equals(rs.getString("nome")))
                                find_category = true;

                        }

                        if(find_category)
                        {
                            dos.writeBytes("-2" + '\n');
                        }

                        else
                        {
                            pstmt = conn.prepareStatement("INSERT INTO Personaggi " + "(nome, hp, biografia, razza, eta, peso, altezza, hp_max, id_sessione, id_utente, is_character, is_npc, is_riding_animal, is_enemy /*Immagine*/ ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");


                            variable_return = "1";

                            pstmt.setString(1, c_name);
                            pstmt.setInt(2, character_hp);
                            pstmt.setString(3, character_biografia);
                            pstmt.setString(4, character_razza);
                            pstmt.setInt(5, character_eta);
                            pstmt.setFloat(6, character_peso);
                            pstmt.setFloat(7, character_altezza);
                            pstmt.setInt(8, character_hp_max);
                            pstmt.setInt(9, session_id);
                            pstmt.setInt(10, client_id);

                            if(what_character.equals("is_character"))
                            {
                                pstmt.setInt(11, 1);
                                pstmt.setInt(12, 0);
                                pstmt.setInt(13, 0);
                                pstmt.setInt(14, 0);
                            }

                            else if(what_character.equals("is_npc"))
                            {
                                pstmt.setInt(11, 0);
                                pstmt.setInt(12, 1);
                                pstmt.setInt(13, 0);
                                pstmt.setInt(14, 0);
                            }

                            else if(what_character.equals("is_riding_animal"))
                            {
                                pstmt.setInt(11, 0);
                                pstmt.setInt(12, 0);
                                pstmt.setInt(13, 1);
                                pstmt.setInt(14, 0);
                            }

                            else if(what_character.equals("is_enemy"))
                            {
                                pstmt.setInt(11, 0);
                                pstmt.setInt(12, 0);
                                pstmt.setInt(13, 0);
                                pstmt.setInt(14, 1);
                            }

                            else
                            {
                                pstmt.setInt(11, 0);
                                pstmt.setInt(12, 0);
                                pstmt.setInt(13, 0);
                                pstmt.setInt(14, 0);
                                variable_return = "-3";
                            }

                            //Inserimento foto

                            pstmt.execute();
                            pstmt.close(); // rilascio le risorse

                            dos.writeBytes(variable_return + '\n');
                        }

                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }


                //Creating session
                else if (received.equals("create_session"))
                {
                    if(is_disconnect == false && is_connect == true)
                    {
                        dos.writeBytes("1" + '\n');

                        s_titolo = dis.readLine();
                        s_sottotitolo = dis.readLine();

                        find_trade = false;
                        rs_temp = stmt.executeQuery("SELECT * FROM Sessioni WHERE Sessioni.titolo = " + s_titolo);
                        while (rs_temp.next()) {
                            find_trade = true;
                        }

                        if(find_trade == false)
                        {
                            find_category = false;

                            do
                            {
                                s_codice_invito = (int)((Math.random()*((9999-1000)+1))+1000);


                                find_category = false;
                                rs_temp = stmt.executeQuery("SELECT * FROM Sessioni WHERE Sessioni.codice_invito = " + s_codice_invito);
                                while (rs_temp.next()) {
                                    find_category = true;
                                }



                            }
                            while(find_category == true);


                            pstmt = conn.prepareStatement("INSERT INTO Sessioni " + "(titolo, sottotitolo, codice_invito, id_host) values (?,?,?,?)");
                            pstmt.setString(1, s_titolo);
                            pstmt.setString(2, s_sottotitolo);
                            pstmt.setInt(3, s_codice_invito);
                            pstmt.setInt(4, client_id);
                            pstmt.execute();
                            pstmt.close(); // rilascio le risorse

                            dos.writeBytes("1" + '\n');

                            dos.writeBytes(Integer.toString(s_codice_invito) + '\n');

                        }

                        else
                        {
                            dos.writeBytes("-2" + '\n');
                        }



                    }
                    else
                    {
                        dos.writeBytes("-1" + '\n');
                    }
                }


                //Add session
                else if (received.equals("add_session"))
                {
                    if(is_disconnect == false && is_connect == true)
                    {
                        dos.writeBytes("1" + '\n');

                        s_codice_invito = Integer.parseInt(dis.readLine());


                        find_trade = false;
                        rs_temp = stmt.executeQuery("SELECT * FROM Sessioni WHERE Sessioni.codice_invito = " + s_codice_invito);
                        while (rs_temp.next()) {
                            find_trade = true;
                            send_id = rs_temp.getInt("id");
                        }

                        if(find_trade == true)
                        {

                            rs_temp = stmt.executeQuery("SELECT * FROM Sessioni WHERE Sessioni.id = " + send_id);
                            while (rs_temp.next()) {

                                dos.writeBytes("1" + '\n');

                                //Inserimento nella tabella relazione con sessione e utente
                                pstmt = conn.prepareStatement("INSERT INTO R_Utente_Sessione " + "(id_utente, id_sessione) values (?,?)");

                                pstmt.setInt(1, client_id);
                                pstmt.setInt(2, send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse


                                //Invio del titolo
                                dos.writeBytes(rs_temp.getString("titolo") + '\n');

                                //Invio del titolo
                                dos.writeBytes(rs_temp.getString("sottotitolo") + '\n');


                                //Calcolo ed invio degli utenti online per quella sessione
                                number_user_online_session = 0;
                                rs = stmt.executeQuery("SELECT * from Utenti u inner join R_Utente_Sessione rus on rus.id_utente = u.id WHERE u.stato_login = 1 AND rus.id_sessione = " + send_id);
                                while (rs.next()) {
                                    number_user_online_session++;
                                }
                                dos.writeBytes(Integer.toString(number_user_online_session) + '\n');




                            }


                        }

                        else
                        {
                            dos.writeBytes("-2" + '\n');
                        }



                    }
                    else
                    {
                        dos.writeBytes("-1" + '\n');
                    }
                }


                //Adding object to equipment
                else if (received.equals("add_object_equipment"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true)
                    {
                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;

                        c_name = dis.readLine();
                        what_character = dis.readLine();


                        rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN R_Personaggio_Oggetto rpo on o.id = rpo.id_oggetto INNER JOIN Personaggio p on p.id = rpo.id_personaggio WHERE p.id_sessione = " + session_id + " AND p.id = " + character_id + " AND o.nome = " + c_name);
                        while (rs.next())
                        {
                            if(c_name.equals(rs.getString("o.nome"))) {
                                find_category = true;
                                send_id = rs.getInt("o.id");
                            }
                        }


                        if(find_category == true)
                        {
                            pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_head_eq = '0', is_torso_eq = '0', is_left_arm_eq = '0', is_right_arm_eq = '0', is_left_leg_eq = '0', is_right_leg_eq = '0', is_first_weapon_eq = '0', is_secondary_weapon_eq = '0', is_gloves_eq = '0', is_left_gloves_eq = '0', is_right_gloves_eq = '0', is_shoes_eq = '0', is_greaves_eq = '0', is_left_greaves_eq = '0', is_right_greaves_eq = '0' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                            pstmt.execute();
                            pstmt.close();


                            if(what_character.equals("head"))
                            {
                                pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_head_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                            }

                            else if(what_character.equals("torso"))
                            {
                                pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_torso_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                            }

                            else if(what_character.equals("left_arm"))
                            {
                                pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_left_arm_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                            }

                            else if(what_character.equals("right_arm"))
                            {
                                pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_right_arm_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                            }

                            else if(what_character.equals("left_leg"))
                            {
                                pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_left_leg_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                            }

                            else if(what_character.equals("right_leg"))
                            {
                                pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_right_leg_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                            }

                            else if(what_character.equals("first_weapon"))
                            {
                                pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_first_weapon_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                            }

                            else if(what_character.equals("secondary_weapon"))
                            {
                                pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_secondary_weapon_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                            }

                            else if(what_character.equals("gloves"))
                            {
                                pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_gloves_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                            }

                            else if(what_character.equals("left_gloves"))
                            {
                                pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_left_gloves_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                            }

                            else if(what_character.equals("right_gloves"))
                            {
                                pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_right_gloves_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                            }

                            else if(what_character.equals("shoes"))
                            {
                                pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_shoes_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                            }

                            else if(what_character.equals("greaves"))
                            {
                                pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_greaves_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                            }

                            else if(what_character.equals("left_greaves"))
                            {
                                pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_left_greaves_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                            }

                            else if(what_character.equals("right_greaves"))
                            {
                                pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_right_greaves_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                            }

                            pstmt.execute();
                            pstmt.close(); // rilascio le risorse

                            dos.writeBytes("1" + '\n');

                        }
                        else
                        {
                            dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        dos.writeBytes("-1" + '\n');
                    }
                }



                //Update hp of character
                else if (received.equals("update_hp"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true)
                    {
                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;

                        character_nome = dis.readLine();
                        character_hp = Integer.parseInt(dis.readLine());


                        rs = stmt.executeQuery("SELECT * from Personaggi p WHERE p.id_sessione = " + session_id + " AND p.nome = " + character_nome);
                        while (rs.next())
                        {

                            if(character_nome.equals(rs.getString("nome"))) {
                                find_category = true;
                                send_id = rs.getInt("id");
                            }
                        }

                        if(find_category == true)
                        {
                            pstmt = conn.prepareStatement("UPDATE Personaggi SET hp = " + character_hp + " where Personaggi.id = " + send_id);
                            pstmt.execute();
                            pstmt.close(); // rilascio le risorse

                            dos.writeBytes("1" + '\n');

                        }
                        else
                        {
                            dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        dos.writeBytes("-1" + '\n');
                    }
                }


                //Update hp_max of character
                else if (received.equals("update_hp_max"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true)
                    {
                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;

                        character_nome = dis.readLine();
                        character_hp_max = Integer.parseInt(dis.readLine());


                        rs = stmt.executeQuery("SELECT * from Personaggi p WHERE p.id_sessione = " + session_id + " AND p.nome = " + character_nome);
                        while (rs.next())
                        {

                            if(character_nome.equals(rs.getString("nome"))) {
                                find_category = true;
                                send_id = rs.getInt("id");
                            }
                        }

                        if(find_category == true)
                        {
                            pstmt = conn.prepareStatement("UPDATE Personaggi SET hp_max = " + character_hp_max + " where Personaggi.id = " + send_id);
                            pstmt.execute();
                            pstmt.close(); // rilascio le risorse

                            dos.writeBytes("1" + '\n');

                        }
                        else
                        {
                            dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        dos.writeBytes("-1" + '\n');
                    }
                }


                //Unlock trading
                else if (received.equals("unlock_trading"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true)
                    {
                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;

                        character_nome = dis.readLine();


                        rs = stmt.executeQuery("SELECT * from Personaggi p WHERE p.id_sessione = " + session_id + " AND p.nome = " + character_nome);
                        while (rs.next())
                        {

                            if(character_nome.equals(rs.getString("nome"))) {
                                find_category = true;
                                send_id = rs.getInt("id");
                            }
                        }

                        if(find_category == true)
                        {
                            pstmt = conn.prepareStatement("UPDATE Personaggi SET is_trading_enabled = '1' where Personaggi.id = " + send_id);
                            pstmt.execute();
                            pstmt.close(); // rilascio le risorse

                            dos.writeBytes("1" + '\n');

                        }
                        else
                        {
                            dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        dos.writeBytes("-1" + '\n');
                    }
                }


                //Unlock trading
                else if (received.equals("lock_trading"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true)
                    {
                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;

                        character_nome = dis.readLine();


                        rs = stmt.executeQuery("SELECT * from Personaggi p WHERE p.id_sessione = " + session_id + " AND p.nome = " + character_nome);
                        while (rs.next())
                        {

                            if(character_nome.equals(rs.getString("nome"))) {
                                find_category = true;
                                send_id = rs.getInt("id");
                            }
                        }

                        if(find_category == true)
                        {
                            pstmt = conn.prepareStatement("UPDATE Personaggi SET is_trading_enabled = '0' where Personaggi.id = " + send_id);
                            pstmt.execute();
                            pstmt.close(); // rilascio le risorse

                            dos.writeBytes("1" + '\n');

                        }
                        else
                        {
                            dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        dos.writeBytes("-1" + '\n');
                    }
                }


                // Game master


                //Avaiable lista di equipaggiamenti di oggetti non equipaggiati


                //Show all items
                else if (received.equals("show_items"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true) {

                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        number_items_b = 0;

                        rs = stmt.executeQuery("SELECT * from Oggetti o WHERE o.id_sessione = " + session_id);
                        while (rs.next()) {
                            number_items_b++;
                        }

                        dos.writeBytes(Integer.toString(number_items_b) + '\n'); // Restituisco il numero di oggetti del backpack

                        image = null;

                        rs = stmt.executeQuery("SELECT * from Oggetti o WHERE rpo.id_sessione = " + session_id);
                        while (rs.next()) {

                            dos.writeBytes(rs.getString("nome") + '\n'); // Restituisco il nome dell'oggetto
                            dos.writeBytes(rs.getString("descrizione") + '\n'); // Restituisco la descrizione dell'oggetto

                            image = rs.getBlob("foto");
                            byte barr[] = image.getBytes(1,(int)image.length());
                            dos.write(barr);

                            //dos.writeBytes( + '\n'); // Restituisco la foto dell'oggetto
                            dos.writeBytes(rs.getString("valore") + '\n'); // Restituisco il valore dell'oggetto

                            dos.writeBytes(rs.getString("quantita") + '\n'); // Restituisco la quantita dell'oggetto
                        }


                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }



                //Update punti skill // DA non fare adesso



                //Show all monsters

                //Show all npcs

                //Show all characters

                //Show all transport






                //Remove object from character
                else if (received.equals("remove_object_character"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {


                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_object = false;

                        o_nome = dis.readLine(); //Mi prendo il nome dell'oggetto
                        ob_quantity = Integer.parseInt(dis.readLine());




                        rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN R_Personaggio_Oggetto rpo on rpo.id_oggetto = o.id WHERE rpo.id_personaggio = " + character_id + " AND o.id_sessione = " + session_id + " AND o.nome = " + o_nome);
                        while (rs.next()) {

                            find_object = true;
                            ob_id = rs.getInt("o.id");
                        }


                        if(find_object == false)
                        {
                            dos.writeBytes("-2" + '\n');
                        }

                        else
                        {
                            pstmt = conn.prepareStatement("DELETE FROM R_Personaggio_Oggetto WHERE id_personaggio = ? AND id_oggetto = ?");
                            pstmt.setInt(1, character_id);
                            pstmt.setInt(2, ob_id);
                            pstmt.execute();
                            pstmt.close(); // rilascio le risorse
                            dos.writeBytes("1" + '\n');
                        }

                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }




                else if (received.equals("update_session_data"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true)
                    {
                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;

                        o_nome = dis.readLine();
                        ob_type = dis.readLine();
                        ob_data = dis.readLine();


                        rs = stmt.executeQuery("SELECT * from Sessioni p WHERE p.titolo = " + o_nome + " AND p.id_host = " + client_id);
                        while (rs.next())
                        {
                            if(o_nome.equals(rs.getString("nome"))) {
                                find_category = true;
                                send_id = rs.getInt("id");
                            }
                        }

                        if(find_category == true)
                        {
                            if(ob_type.equals("titolo"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Sessioni SET titolo = " + ob_data + " where Sessioni.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("sottotitolo"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Sessioni SET sottotitolo = " + ob_data + " where Sessioni.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }


                            //UPDATE FOTO

                            else
                            {
                                dos.writeBytes("-3" + '\n');
                            }

                        }
                        else
                        {
                            dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        dos.writeBytes("-1" + '\n');
                    }
                }


                else if (received.equals("get_category_data"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true)
                    {
                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;

                        o_nome = dis.readLine();
                        ob_type = dis.readLine();


                        rs = stmt.executeQuery("SELECT * from Categorie p WHERE p.id_sessione = " + session_id + " AND p.nome = " + o_nome);
                        while (rs.next())
                        {
                            if(o_nome.equals(rs.getString("nome"))) {
                                find_category = true;
                                send_id = rs.getInt("id");
                            }
                        }

                        if(find_category == true)
                        {

                            rs = stmt.executeQuery("SELECT * from Categorie p WHERE p.id_sessione = " + session_id + " AND p.id = " + send_id);
                            while (rs.next())
                            {

                                if(ob_type.equals("descrizione"))
                                {
                                    dos.writeBytes("1" + '\n');
                                    dos.writeBytes(rs.getString("descrizione") + '\n');
                                }


                                //GET FOTO


                                else
                                {
                                    dos.writeBytes("-3" + '\n');
                                }
                            }



                        }
                        else
                        {
                            dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        dos.writeBytes("-1" + '\n');
                    }
                }


                else if (received.equals("update_category_data"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true)
                    {
                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;

                        o_nome = dis.readLine();
                        ob_type = dis.readLine();
                        ob_data = dis.readLine();


                        rs = stmt.executeQuery("SELECT * from Categorie p WHERE p.id_sessione = " + session_id + " AND p.nome = " + o_nome);
                        while (rs.next())
                        {
                            if(o_nome.equals(rs.getString("nome"))) {
                                find_category = true;
                                send_id = rs.getInt("id");
                            }
                        }

                        if(find_category == true)
                        {
                            if(ob_type.equals("nome"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET nome = " + ob_data + " where Categorie.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("descrizione"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET descrizione = " + ob_data + " where Categorie.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }


                            //UPDATE FOTO

                            else
                            {
                                dos.writeBytes("-3" + '\n');
                            }

                        }
                        else
                        {
                            dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        dos.writeBytes("-1" + '\n');
                    }
                }



                //Update character data

                else if (received.equals("get_personaggi_data"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true)
                    {
                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;

                        o_nome = dis.readLine();
                        ob_type = dis.readLine();


                        rs = stmt.executeQuery("SELECT * from Personaggi p WHERE p.id_sessione = " + session_id + " AND p.nome = " + o_nome);
                        while (rs.next())
                        {

                            if(o_nome.equals(rs.getString("nome"))) {
                                find_category = true;
                                send_id = rs.getInt("id");
                            }
                        }

                        if(find_category == true)
                        {

                            rs = stmt.executeQuery("SELECT * from Personaggi p WHERE p.id_sessione = " + session_id + " AND p.id = " + send_id);
                            while (rs.next())
                            {

                                if(ob_type.equals("hp"))
                                {
                                    dos.writeBytes("1" + '\n');
                                    dos.writeBytes( Integer.toString(rs.getInt("hp")) + '\n');
                                }

                                else if(ob_type.equals("biografia"))
                                {
                                    dos.writeBytes("1" + '\n');
                                    dos.writeBytes(rs.getString("biografia") + '\n');
                                }

                                else if(ob_type.equals("razza"))
                                {
                                    dos.writeBytes("1" + '\n');
                                    dos.writeBytes(rs.getString("razza") + '\n');
                                }

                                else if(ob_type.equals("eta"))
                                {
                                    dos.writeBytes("1" + '\n');
                                    dos.writeBytes( Integer.toString(rs.getInt("eta")) + '\n');
                                }

                                else if(ob_type.equals("peso"))
                                {
                                    dos.writeBytes("1" + '\n');
                                    dos.writeBytes( Float.toString(rs.getFloat("peso")) + '\n');
                                }

                                else if(ob_type.equals("altezza"))
                                {
                                    dos.writeBytes("1" + '\n');
                                    dos.writeBytes( Float.toString(rs.getFloat("altezza")) + '\n');
                                }

                                //GET FOTO


                                else if(ob_type.equals("hp_max"))
                                {
                                    dos.writeBytes("1" + '\n');
                                    dos.writeBytes( Integer.toString(rs.getInt("hp_max")) + '\n');
                                }

                                else if(ob_type.equals("is_character"))
                                {
                                    dos.writeBytes("1" + '\n');
                                    dos.writeBytes( Integer.toString(rs.getInt("is_character")) + '\n');
                                }

                                else if(ob_type.equals("is_npc"))
                                {
                                    dos.writeBytes("1" + '\n');
                                    dos.writeBytes( Integer.toString(rs.getInt("is_npc")) + '\n');
                                }

                                else if(ob_type.equals("is_riding_animal"))
                                {
                                    dos.writeBytes("1" + '\n');
                                    dos.writeBytes( Integer.toString(rs.getInt("is_riding_animal")) + '\n');
                                }


                                else if(ob_type.equals("is_enemy"))
                                {
                                    dos.writeBytes("1" + '\n');
                                    dos.writeBytes( Integer.toString(rs.getInt("is_enemy")) + '\n');
                                }


                                else if(ob_type.equals("punti_skill"))
                                {
                                    dos.writeBytes("1" + '\n');
                                    dos.writeBytes( Integer.toString(rs.getInt("punti_skill")) + '\n');
                                }

                                else if(ob_type.equals("is_enabled"))
                                {
                                    dos.writeBytes("1" + '\n');
                                    dos.writeBytes( Integer.toString(rs.getInt("is_enabled")) + '\n');
                                }


                                else
                                {
                                    dos.writeBytes("-3" + '\n');
                                }
                            }



                        }
                        else
                        {
                            dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        dos.writeBytes("-1" + '\n');
                    }
                }


                else if (received.equals("update_personaggi_data"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true)
                    {
                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;

                        o_nome = dis.readLine();
                        ob_type = dis.readLine();
                        ob_data = dis.readLine();


                        rs = stmt.executeQuery("SELECT * from Personaggi p WHERE p.id_sessione = " + session_id + " AND p.nome = " + o_nome);
                        while (rs.next())
                        {

                            if(o_nome.equals(rs.getString("nome"))) {
                                find_category = true;
                                send_id = rs.getInt("id");
                            }
                        }

                        if(find_category == true)
                        {
                            if(ob_type.equals("nome"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET nome = " + ob_data + " where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("hp"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET hp = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("biografia"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET biografia = " + ob_data + " where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("razza"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET razza = " + ob_data + " where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("eta"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET eta = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("peso"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET peso = '" + Float.parseFloat(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("altezza"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET altezza = '" + Float.parseFloat(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }

                            //UPDATE FOTO




                            //GAME MASTER ADD ON

                            else if(ob_type.equals("hp_max"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET hp_max = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("is_character"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET is_character = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("is_npc"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET is_npc = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("is_riding_animal"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET is_riding_animal = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }


                            else if(ob_type.equals("is_enemy"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET is_enemy = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }


                            else if(ob_type.equals("punti_skill"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET punti_skill = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("is_enabled"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET is_enabled = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }


                            else
                            {
                                dos.writeBytes("-3" + '\n');
                            }

                        }
                        else
                        {
                            dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        dos.writeBytes("-1" + '\n');
                    }
                }


                else if (received.equals("get_object_data"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true)
                    {
                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;

                        o_nome = dis.readLine();
                        ob_type = dis.readLine();


                        rs = stmt.executeQuery("SELECT * from Oggetti o WHERE o.id_sessione = " + session_id + " AND o.nome = " + o_nome);
                        while (rs.next())
                        {

                            if(o_nome.equals(rs.getString("nome"))) {
                                find_category = true;
                                send_id = rs.getInt("id");
                            }
                        }

                        if(find_category == true)
                        {

                            rs = stmt.executeQuery("SELECT * from Oggetti o WHERE o.id_sessione = " + session_id + " AND o.id = " + send_id);
                            while (rs.next())
                            {

                                if(ob_type.equals("descrizione"))
                                {
                                    dos.writeBytes("1" + '\n');
                                    dos.writeBytes(rs.getString("descrizione") + '\n');
                                }

                                else if(ob_type.equals("valore"))
                                {
                                    dos.writeBytes("1" + '\n');
                                    dos.writeBytes( Float.toString(rs.getFloat("valore")) + '\n');
                                }

                                else if(ob_type.equals("campo1"))
                                {
                                    dos.writeBytes("1" + '\n');
                                    dos.writeBytes(rs.getString("campo1") + '\n');
                                }

                                else if(ob_type.equals("campo2"))
                                {
                                    dos.writeBytes("1" + '\n');
                                    dos.writeBytes(rs.getString("campo2") + '\n');
                                }

                                else if(ob_type.equals("campo3"))
                                {
                                    dos.writeBytes("1" + '\n');
                                    dos.writeBytes(rs.getString("campo3") + '\n');
                                }

                                else if(ob_type.equals("campo4"))
                                {
                                    dos.writeBytes("1" + '\n');
                                    dos.writeBytes(rs.getString("campo4") + '\n');
                                }

                                else if(ob_type.equals("campo5"))
                                {
                                    dos.writeBytes("1" + '\n');
                                    dos.writeBytes(rs.getString("campo5") + '\n');
                                }

                                else if(ob_type.equals("rarita_colore"))
                                {
                                    dos.writeBytes("1" + '\n');
                                    dos.writeBytes(rs.getString("rarita_colore") + '\n');
                                }

                                //GET FOTO

                                else
                                {
                                    dos.writeBytes("-3" + '\n');
                                }
                            }



                        }
                        else
                        {
                            dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        dos.writeBytes("-1" + '\n');
                    }
                }

                else if (received.equals("update_object_data"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true)
                    {
                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;

                        o_nome = dis.readLine();
                        ob_type = dis.readLine();
                        ob_data = dis.readLine();


                        rs = stmt.executeQuery("SELECT * from Oggetti o WHERE o.id_sessione = " + session_id + " AND o.nome = " + o_nome);
                        while (rs.next())
                        {

                            if(o_nome.equals(rs.getString("nome"))) {
                                find_category = true;
                                send_id = rs.getInt("id");
                            }
                        }

                        if(find_category == true)
                        {
                            if(ob_type.equals("nome"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Oggetti SET nome = " + ob_data + " where Oggetti.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("descrizione"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Oggetti SET descrizione = " + ob_data + " where Oggetti.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("valore"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Oggetti SET valore = '" + Float.parseFloat(ob_data) + "' where Oggetti.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("campo1"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Oggetti SET campo1 = " + ob_data + " where Oggetti.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("campo2"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Oggetti SET campo2 = " + ob_data + " where Oggetti.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("campo3"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Oggetti SET campo3 = " + ob_data + " where Oggetti.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("campo4"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Oggetti SET campo4 = " + ob_data + " where Oggetti.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("campo5"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Oggetti SET campo5 = " + ob_data + " where Oggetti.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("rarita"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Oggetti SET rarita_colore = " + ob_data + " where Oggetti.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                dos.writeBytes("1" + '\n');
                            }

                            //UPDATE FOTO


                            else
                            {
                                dos.writeBytes("-3" + '\n');
                            }

                        }
                        else
                        {
                            dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        dos.writeBytes("-1" + '\n');
                    }
                }




                //ADD object to category
                else if (received.equals("add_object_category"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true) {


                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;
                        find_object = false;

                        c_name = dis.readLine(); //Mi prendo il nome della category
                        o_nome = dis.readLine(); //Mi prendo il nome dell'oggetto
                        ob_quantity = Integer.parseInt(dis.readLine());


                        rs = stmt.executeQuery("SELECT * from Categorie c WHERE c.id_sessione = " + session_id + " AND c.nome = " + c_name);
                        while (rs.next()) {
                            find_character = true;
                            ch_id = rs.getInt("id");
                        }

                        rs = stmt.executeQuery("SELECT * from Oggetti o WHERE o.id_sessione = " + session_id + " AND o.nome = " + o_nome);
                        while (rs.next()) {

                            find_object = true;
                            ob_id = rs.getInt("id");
                        }

                        if(find_category == false)
                        {
                            dos.writeBytes("-2" + '\n');
                        }

                        else if(find_object == false)
                        {
                            dos.writeBytes("-3" + '\n');
                        }

                        else
                        {
                            find_object = false;
                            rs = stmt.executeQuery("SELECT * from Oggetti o WHERE o.id = " + ob_id + " AND o.id_categoria = " + ch_id);
                            while (rs.next()) {
                                find_object = true;
                            }

                            if(find_object == false)
                            {
                                pstmt = conn.prepareStatement("UPDATE Oggetti SET id_categoria = '" + ch_id + "' WHERE o.id = " + ob_id + " AND o.id_sessione = " + session_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse

                                dos.writeBytes("1" + '\n');
                            }

                            else
                            {
                                dos.writeBytes("-4" + '\n');
                            }

                        }

                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }

                //ADD object to character
                else if (received.equals("add_object_character"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true) {


                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_character = false;
                        find_object = false;

                        c_name = dis.readLine(); //Mi prendo il nome del character
                        o_nome = dis.readLine(); //Mi prendo il nome dell'oggetto
                        ob_quantity = Integer.parseInt(dis.readLine());




                        rs = stmt.executeQuery("SELECT * from Personaggi p WHERE p.id_sessione = " + session_id + " AND p.nome = " + c_name);
                        while (rs.next()) {

                            find_character = true;
                            ch_id = rs.getInt("id");
                        }

                        rs = stmt.executeQuery("SELECT * from Oggetti o WHERE o.id_sessione = " + session_id + " AND o.nome = " + o_nome);
                        while (rs.next()) {

                            find_object = true;
                            ob_id = rs.getInt("id");
                        }

                        if(find_character == false)
                        {
                            dos.writeBytes("-2" + '\n');
                        }

                        else if(find_object == false)
                        {
                            dos.writeBytes("-3" + '\n');
                        }

                        else
                        {
                            find_category = false;
                            rs = stmt.executeQuery("SELECT * from R_Personaggio_Oggetto p WHERE p.id_personaggio = " + ch_id + " AND p.id_oggetto = " + ob_id);
                            while (rs.next()) {
                                find_category = true;
                                ob_numbers = rs.getInt("quantita");
                            }

                            if(find_category == true)
                            {

                                ob_quantity = ob_quantity + ob_numbers;

                                pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET quantita = '" + ob_quantity + "' WHERE p.id_personaggio = " + ch_id + " AND p.id_oggetto = " + ob_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse

                            }

                            else
                            {
                                pstmt = conn.prepareStatement("INSERT INTO R_Personaggio_Oggetto " + "(id_personaggio, id_oggetto, quantita) values (?,?,?)");
                                pstmt.setInt(1, ch_id);
                                pstmt.setInt(2, ob_id);
                                pstmt.setInt(3, ob_quantity);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse

                            }

                            dos.writeBytes("1" + '\n');
                        }

                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }

                //Creating object
                else if (received.equals("create_object"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true) {


                        dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;

                        o_nome = dis.readLine(); //Mi prendo il nome dell'oggetto
                        o_descrizione = dis.readLine(); //Mi prendo la descrizione dell'oggetto
                        o_rarita = dis.readLine();
                        o_campo1 = dis.readLine();
                        o_campo2 = dis.readLine();
                        o_campo3 = dis.readLine();
                        o_campo4 = dis.readLine();
                        o_campo5 = dis.readLine();
                        o_valore = Float.parseFloat(dis.readLine());
                        //FOTO


                        rs = stmt.executeQuery("SELECT * from Oggetti c WHERE o.id_sessione = " + session_id);
                        while (rs.next()) {

                            if(o_nome.equals(rs.getString("nome")))
                                find_category = true;

                        }

                        if(find_category)
                        {
                            dos.writeBytes("-2" + '\n');
                        }

                        else
                        {
                            pstmt = conn.prepareStatement("INSERT INTO Oggetti " + "(nome, descrizione, valore, campo1, campo2, campo3, campo4, campo5, rarita_colore, id_sessione) values (?,?,?,?,?,?,?,?,?,?)");


                            pstmt.setString(1, o_nome);
                            pstmt.setString(2, o_descrizione);
                            pstmt.setFloat(3, o_valore);
                            pstmt.setString(4, o_campo1);
                            pstmt.setString(5, o_campo2);
                            pstmt.setString(6, o_campo3);
                            pstmt.setString(7, o_campo4);
                            pstmt.setString(8, o_campo5);
                            pstmt.setString(9, o_rarita);
                            pstmt.setInt(10, session_id);
                            pstmt.execute();
                            pstmt.close(); // rilascio le risorse

                            dos.writeBytes("1" + '\n');
                        }

                    }
                    else
                        dos.writeBytes("-1" + '\n');
                }


                else if(received.equals("get_user_id"))
                {
                	//CONTROLLO SE L'UTENTE ESISTE NEL DATABASE
                	send_username = dis.readLine();

                	if(username.equals(send_username))
                    {
                        variable_return = "-5";
                    }
                    else
                    {
                        try {

                            find_user = false;

                            // recupero i dati
                            rs = stmt.executeQuery("SELECT * from Utenti");

                            while (rs.next() && find_user == false) {
                                if (send_username.equals(rs.getString("nome_utente"))) {
                                    find_user = true;
                                    send_id = rs.getInt("id");

                                }

                            }

                            if (find_user) 
                            {
                            	variable_return = Integer.toString(send_id);
                            }
                            else
                            {
                            	variable_return = "-2";
                            }
                            
                        }
                        catch(SQLException e)
                        {
                            System.out.println(e);
                        }

                    }
                    dos.writeBytes(variable_return + '\n');
                    
                }

                else if(received.equals("create_user"))
                {
                    //INVIO MESSAGGIO ALL'UTENTE DESIDERATO



                    //DATA CAMBIARE
                    new_username = dis.readLine();
                    new_password = dis.readLine();
                    //new_name = dis.readLine();
                    //new_surname = dis.readLine();
                    new_email = dis.readLine();



                    try {

                        find_user = false;
                        my_bool = false;

                        if(is_connect == false && is_disconnect) {
                            Class.forName("com.mysql.jdbc.Driver");
                            conn = DriverManager.getConnection("jdbc:mysql://216.158.239.6/game?" + "user=inventorymaster&password=Rootinventory1!");
                            stmt = conn.createStatement();
                            my_bool = true;
                        }
                        // creo la tabella
                        // recupero i dati
                        rs = stmt.executeQuery("SELECT * from Utenti");

                        while(rs.next() && find_user == false)
                        {
                            if(new_username.equals(rs.getString("nome_utente")))
                            {
                                find_user = true;

                            }
                        }

                        if(find_user)
                        {
                            variable_return = "-2";
                        }

                        else
                        {

                            pstmt = conn.prepareStatement("INSERT INTO Utenti " + "(nome_utente, password, email) values (?,?,?)");

                            //pstmt.setString(1, new_name);
                            //pstmt.setString(2, new_surname);
                            pstmt.setString(1, new_username);
                            pstmt.setString(2, new_password);
                            pstmt.setString(3, new_email);
                            pstmt.execute();
                            pstmt.close(); // rilascio le risorse
                            variable_return = "1";
                        }

                        System.out.println("Restituisco variabile inserimento utente " + variable_return);
                        dos.writeBytes(variable_return + '\n');

                        if(my_bool)
                        {
                            my_bool = false;
                            stmt.close(); // rilascio le risorse
                            conn.close(); // termino la connessione
                        }
                    }
                    catch(SQLException e)
                    {
                        System.out.println(e);
                        variable_return = "-1";
                        dos.writeBytes(variable_return + '\n');
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }

                else if(received.equals("disconnect")) 
                { 
                	
                	//UTENTE DISCONNESSO
                	if(is_disconnect == false && is_connect == true) {

                	    System.out.println("STO EFFETTUANDO LA DISCONNESSIONE");

                        is_connect = false;
                        is_disconnect = true;
                        is_session_connected = false;
                        is_character_connected = false;


                        pstmt = conn.prepareStatement("UPDATE Utenti SET stato_login = '0' where Utenti.id = " + client_id);
                        pstmt.execute();

                        /*pstmt = conn.prepareStatement("UPDATE Utenti SET ultimo_login = CURRENT_TIMESTAMP where Utenti.id = " + client_id);
                        pstmt.execute();*/

                        dos.writeBytes("1" + '\n');
                    }
                	else
                        dos.writeBytes("-1" + '\n');

                }



                else if(received.equals("close_connection")) 
                {  
                	//CHIUSURA DELLA CONNESSIONE
                	
                	if(is_disconnect == true)
                	{
                		is_closed = true;
	                    System.out.println("Client " + this.s + " want to close the connection..."); 
	                    System.out.println("Closing this connection.");
	                    dos.writeBytes("1" + '\n');
	                    this.s.close(); 
	                    System.out.println("Connection closed");
	
	
	                    pstmt.close(); // rilascio le risorse
	                    stmt.close(); // rilascio le risorse
	                    conn.close(); // termino la connessione
                	}
                	else
                	{
                		dos.writeBytes("-4" + '\n');
                	}
                    break; 
                }


                /*
                // creating Date object 
                Date date = new Date(); 
                  
                // write on output stream based on the 
                // answer from the client 
                switch (received) { 
                  
                    case "Date" : 
                        toreturn = fordate.format(date); 
                        dos.writeUTF(toreturn); 
                        break; 
                          
                    case "Time" : 
                        toreturn = fortime.format(date); 
                        dos.writeUTF(toreturn); 
                        break; 
                          
                    default: 
                        dos.writeUTF("Invalid input"); 
                        break; 
                } */


                else if(received.equals("show_users_online"))
                {

                    //VISUALIZZO GLI UTENTI ONLINE

                    System.out.println("\nClient " + this.s + " want to know the users online");


                    try {

                        my_bool = false;

                        if(is_connect == false && is_disconnect) {
                            Class.forName("com.mysql.jdbc.Driver");
                            conn = DriverManager.getConnection("jdbc:mysql://216.158.239.6/game?" + "user=inventorymaster&password=Rootinventory1!");
                            stmt = conn.createStatement();
                            my_bool = true;
                        }
                        // creo la tabella


                        number_users_online = 0;

                        rs = stmt.executeQuery("SELECT * from Utenti where Utenti.stato_login = '1'");

                        while(rs.next())
                        {
                            number_users_online++;
                        }


                        dos.writeBytes(Integer.toString(number_users_online) + '\n');


                        // creo la tabella
                        if(number_users_online > 0) {
                            rs = stmt.executeQuery("SELECT * from Utenti where Utenti.stato_login = '1'");

                            toreturn = "";

                            while (rs.next()) {
                                toreturn = rs.getString("nome_utente");
                                //toreturn = " NAME: " + rs.getString("nome") + "  SURNAME: " + rs.getString("cognome") + "  USERNAME: " + rs.getString("nome_utente");
                                System.out.println(toreturn);
                                dos.writeBytes(toreturn + '\n');

                            }

                        }

                        if(my_bool)
                        {
                            my_bool = false;
                            stmt.close(); // rilascio le risorse
                            conn.close(); // termino la connessione
                        }

                    }
                    catch(SQLException e)
                    {
                        System.out.println(e);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }

                else
                {
                    //System.out.println("CIAO");
                    System.out.println("\n\n Received: " + received + "\n\n");
                }

            } catch (IOException e) { 
                e.printStackTrace(); 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } 
          
        try
        { 
            // closing resources 
            this.dis.close(); 
            this.dos.close();
              
        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
    } 
}