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

        String id_passed_session = "";
        String id_passed_character = "";

        String filter_item = "";

        int i = 0;
        boolean is_session_connected = false;
        boolean is_character_connected = false;
        boolean is_host = false;

        Blob image = null;

        int session_id = 0;
        int character_id = 0;

        String new_username = "", new_password = "", new_name = "", new_surname = "", new_email = "";

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

                else if (received.equals("get_user_backpack"))
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