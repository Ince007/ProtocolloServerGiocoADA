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
        ServerSocket ss = new ServerSocket(8181);
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
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));


                System.out.println("Assigning new thread for this client"); 
  
                // create a new thread object 
                t[number_client_connected] = new ClientHandler(s, dis, dos, number_client_connected, writer);


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


    private boolean is_ios = false;




    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd"); 
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss"); 
    //final DataInputStream dis;
    final BufferedReader dis;
    final DataOutputStream dos;
    final BufferedWriter writer;
    final Socket s;
    final int number;




  
    // Constructor 
    public ClientHandler(Socket s, BufferedReader dis, DataOutputStream dos, int number, BufferedWriter writer)
    {
        this.number = number;
        this.s = s; 
        this.dis = dis; 
        this.dos = dos;
        this.writer = writer;


    }

    private void sendMessage(String message) throws InterruptedException, IOException {
        //sleep(115);
        if(is_ios == true) {
            sleep(150);
            dos.write(message.getBytes(), 0, message.getBytes().length);
            dos.flush();
        }
        else
        {
            dos.writeBytes(message + '\n');
        }
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

        String c_name = "", c_desc = "", c_type = "", c_tipo_potenza = "";
        int c_numero_slot = 0;
        String c_query1 = "", c_query2 = "", c_query3 = "";
        int c_number = 0;
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

        float ch_peso = 0;


        String character_nome = "", character_biografia = "", character_razza = "";
        int character_hp = 0, character_eta = 0, character_hp_max;
        float character_peso = 0, character_altezza = 0;
        String what_character = "";

        String character_capelli = "", character_occhi = "", character_mark = "", character_cicatrici = "", character_tatuaggi = "", character_luogo_nascita = "", character_sesso = "", character_colore_pelle = "", character_classe = "";
        int character_anno_nascita = 0, character_giorno_nascita = 0, character_mese_nascita = 0;


        String new_username = "", new_password = "", new_name = "", new_surname = "", new_email = "";


        String s_titolo = "", s_sottotitolo = "";
        int s_codice_invito = 0;


        String o_nome = "", o_descrizione = "", o_campo1 = "", o_campo2 = "", o_campo3 = "", o_campo4 = "", o_campo5 = "", o_rarita = "";
        float o_peso = 0, o_potenza = 0;
        int o_durabilita = 0;
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

        boolean can_be = false;

        int slot = 0, slot2 = 0;


        is_ios = false;

        try {
            received = dis.readLine();
            if(received.equals("ios"))
            {
                is_ios = true;
            }
            else
            {
                is_ios = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (is_closed == false)
        { 
            try {

                // receive the answer from client 
                received = dis.readLine();



                //USER

                if(received.equals("connect"))      //CONNECT
                {
                    if(is_connect == false && is_disconnect == true)
                    {

                        //CONNESSIONE AL DATABASE

                        System.out.println("\nClient " + this.s + " want to connect to the database");


                        username = dis.readLine();
                        password = dis.readLine();


                        System.out.println("Username: " + username);
                        System.out.println("Password: " + password);



                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                            conn = DriverManager.getConnection("jdbc:mysql://216.158.239.6/game?" + "user=inventorymaster&password=Rootinventory1!");

                            //conn = DriverManager.getConnection("jdbc:mysql://localhost/game?" + "user=root&password=");

                            // creo la tabella
                            stmt = conn.createStatement();

                            conn_temp = DriverManager.getConnection("jdbc:mysql://216.158.239.6/game?" + "user=inventorymaster&password=Rootinventory1!");
                            stmt_temp = conn_temp.createStatement();




                            this.s.setKeepAlive(true);



                            find_user = false;

                            // recupero i dati
                            rs = stmt.executeQuery("SELECT * from Utenti");

                            //Controllo nel database dell'esistenza dell'utente e assegnazione di quest'ultimo ad 1 dello stato di login
                            while(rs.next() && find_user == false)
                            {
                                if(username.equals(rs.getString("nome_utente")) && password.equals(rs.getString("password")))
                                {
                                    find_user = true;
                                    client_id = rs.getInt("id");
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

                                System.out.println("\n\nRestituisco 1");
                                sendMessage("1");

                                System.out.println("\nNumber Sessions: " + Integer.toString(number_sessions));
                                sendMessage(Integer.toString(number_sessions));
                                //dos.writeBytes(Integer.toString(number_sessions) + '\n');

                                if(number_sessions != 0) {
                                    rs = stmt.executeQuery("SELECT * from Sessioni s inner join R_Utente_Sessione rus on rus.id_sessione = s.id where rus.id_utente = " + client_id);
                                    while (rs.next()) {

                                        //Invio del titolo
                                        // dos.writeBytes(rs.getString("titolo") + '\n');
                                        System.out.println("\n\nTitolo: " + rs.getString("titolo"));
                                        sendMessage("T:" + rs.getString("titolo"));

                                        //Invio del sottotitolo
                                        //dos.writeBytes(rs.getString("sottotitolo") + '\n');

                                        if (rs.getString("sottotitolo") == null) {
                                            sendMessage("S:0");
                                        } else {
                                            System.out.println("SottoTitolo: " + rs.getString("sottotitolo"));
                                            sendMessage("S:" + rs.getString("sottotitolo"));
                                        }


                                        //Calcolo ed invio degli utenti online per quella sessione
                                        number_user_online_session = 0;
                                        rs_temp = stmt_temp.executeQuery("SELECT * from Utenti u inner join R_Utente_Sessione rus on rus.id_utente = u.id WHERE u.stato_login = 1 AND rus.id_sessione = " + rs.getString("id"));
                                        while (rs_temp.next()) {
                                            number_user_online_session++;
                                        }
                                        //   dos.writeBytes(Integer.toString(number_user_online_session) + '\n');
                                        System.out.println("Number Online user: " + Integer.toString(number_user_online_session));
                                        sendMessage("NU:" + Integer.toString(number_user_online_session));


                                    }


                                }
                            }

                            else
                            {
                                sendMessage("-2");
                                is_connect = false;
                                is_disconnect = true;

                            }

                            //pstmt.close(); // rilascio le risorse
	                        /*stmt.close(); // rilascio le risorse
	                        conn.close(); // termino la connessione*/

                        }
                        catch(ClassNotFoundException e)
                        {
                            System.out.println(e);
                        }
                        catch(SQLException e)
                        {
                            System.out.println(e);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');



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

                        sendMessage("1");
                        //dos.writeBytes("1" + '\n');
                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');

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
                    sendMessage(variable_return);
                    //dos.writeBytes(variable_return + '\n');

                }

                else if(received.equals("create_user"))
                {
                    //INVIO MESSAGGIO ALL'UTENTE DESIDERATO


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
                        sendMessage(variable_return);
                        //dos.writeBytes(variable_return + '\n');

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
                        sendMessage(variable_return);
                        //dos.writeBytes(variable_return + '\n');
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }

                else if(received.equals("update_user"))
                {
                    ob_type = dis.readLine();
                    ob_data = dis.readLine();


                    if(is_disconnect == false && is_connect == true) {
                        sendMessage("1");

                        if(ob_type.equals("nome_utente"))
                        {
                            pstmt = conn.prepareStatement("UPDATE Utenti SET nome_utente = '" + ob_data + "' where Utenti.id = " + client_id);
                            pstmt.execute();
                            pstmt.close(); // rilascio le risorse
                            sendMessage("1");
                        }

                        else if(ob_type.equals("password"))
                        {
                            pstmt = conn.prepareStatement("UPDATE Utenti SET password = '" + ob_data + "' where Utenti.id = " + client_id);
                            pstmt.execute();
                            pstmt.close(); // rilascio le risorse
                            sendMessage("1");
                        }

                        else if(ob_type.equals("email"))
                        {
                            pstmt = conn.prepareStatement("UPDATE Utenti SET email = '" + ob_data + "' where Utenti.id = " + client_id);
                            pstmt.execute();
                            pstmt.close(); // rilascio le risorse
                            sendMessage("1");
                        }

                        //Update immagine di profilo

                        else
                        {
                            sendMessage("-2");
                        }

                    }
                    else
                        sendMessage("-1");

                }




                //CATEGORY

                else if (received.equals("create_category"))
                {

                    c_name = dis.readLine(); //Mi prendo il nome della categoria
                    c_desc = dis.readLine(); //Mi prendo la descrizione della categoria
                    c_tipo_potenza = dis.readLine();
                    c_numero_slot = Integer.parseInt(dis.readLine());

                    //Mi prendo il numero di possibili equipaggiamenti per quella categoria
                    c_number = Integer.parseInt(dis.readLine());


                    c_type = dis.readLine(); // Mi prendo il tipo di categoria


                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true) {


                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;



                        rs = stmt.executeQuery("SELECT * from Categorie c WHERE c.id_sessione = " + session_id);
                        while (rs.next()) {

                            if(c_name.equals(rs.getString("nome")))
                                find_category = true;

                        }

                        if(find_category)
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }

                        else
                        {
                            pstmt = conn.prepareStatement("INSERT INTO Categorie " + "(nome, descrizione, id_sessione, tipo_potenza, numero_slot) values (?,?,?,?,?)");
                            pstmt.setString(1, c_name);
                            pstmt.setString(2, c_desc);
                            pstmt.setInt(3, session_id);
                            pstmt.setString(4, c_tipo_potenza);
                            pstmt.setInt(5, c_numero_slot);
                            pstmt.execute();
                            pstmt.close(); // rilascio le risorse

                            rs = stmt.executeQuery("SELECT * from Categorie c WHERE c.id_sessione = " + session_id);
                            while (rs.next()) {

                                if(c_name.equals(rs.getString("nome")))
                                    send_id = rs.getInt("id");

                            }

                            sendMessage("1");
                            //dos.writeBytes("1" + '\n'); // Restituisco che è andato tutto a buon fine

                            for(i = 0; i < c_number; i++)
                            {


                                if(c_type.equals("head"))
                                {
                                    pstmt = conn.prepareStatement("UPDATE Categorie SET is_head_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                }

                                else if(c_type.equals("torso"))
                                {
                                    pstmt = conn.prepareStatement("UPDATE Categorie SET is_torso_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                }

                                else if(c_type.equals("left_arm"))
                                {
                                    pstmt = conn.prepareStatement("UPDATE Categorie SET is_left_arm_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                }

                                else if(c_type.equals("right_arm"))
                                {
                                    pstmt = conn.prepareStatement("UPDATE Categorie SET is_right_arm_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                }

                                else if(c_type.equals("left_leg"))
                                {
                                    pstmt = conn.prepareStatement("UPDATE Categorie SET is_left_leg_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                }

                                else if(c_type.equals("right_leg"))
                                {
                                    pstmt = conn.prepareStatement("UPDATE Categorie SET is_right_leg_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                }

                                else if(c_type.equals("first_weapon"))
                                {
                                    pstmt = conn.prepareStatement("UPDATE Categorie SET is_first_weapon_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                }

                                else if(c_type.equals("secondary_weapon"))
                                {
                                    pstmt = conn.prepareStatement("UPDATE Categorie SET is_secondary_weapon_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                }

                                else if(c_type.equals("gloves"))
                                {
                                    pstmt = conn.prepareStatement("UPDATE Categorie SET is_gloves_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                }

                                else if(c_type.equals("left_gloves"))
                                {
                                    pstmt = conn.prepareStatement("UPDATE Categorie SET is_left_gloves_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                }

                                else if(c_type.equals("right_gloves"))
                                {
                                    pstmt = conn.prepareStatement("UPDATE Categorie SET is_right_gloves_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                }

                                else if(c_type.equals("shoes"))
                                {
                                    pstmt = conn.prepareStatement("UPDATE Categorie SET is_shoes_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                }

                                else if(c_type.equals("greaves"))
                                {
                                    pstmt = conn.prepareStatement("UPDATE Categorie SET is_greaves_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                }

                                else if(c_type.equals("left_greaves"))
                                {
                                    pstmt = conn.prepareStatement("UPDATE Categorie SET is_left_greaves_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                }

                                else if(c_type.equals("right_greaves"))
                                {
                                    pstmt = conn.prepareStatement("UPDATE Categorie SET is_right_greaves_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                }

                                else if(c_type.equals("magic"))
                                {
                                    pstmt = conn.prepareStatement("UPDATE Categorie SET is_magic_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                }

                                else if(c_type.equals("other"))
                                {
                                    pstmt = conn.prepareStatement("UPDATE Categorie SET is_other_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                }

                                else if(c_type.equals("arms"))
                                {
                                    pstmt = conn.prepareStatement("UPDATE Categorie SET is_arms_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                }

                                else if(c_type.equals("legs"))
                                {
                                    pstmt = conn.prepareStatement("UPDATE Categorie SET is_legs_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                }

                                else if(c_type.equals("ammo"))
                                {
                                    pstmt = conn.prepareStatement("UPDATE Categorie SET is_ammo_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                }

                            }

                            sendMessage("1");
                            //dos.writeBytes("1" + '\n');
                        }

                    }
                    else
                        sendMessage("-1");
                        //dos.writeBytes("-1" + '\n');
                }


                else if (received.equals("show_category"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true) {


                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        number_categories = 0;

                        rs = stmt.executeQuery("SELECT * from Categorie c WHERE c.id_sessione = " + session_id);
                        while (rs.next()) {

                            number_categories++;

                        }

                        sendMessage(Integer.toString(number_categories));
                        //dos.writeBytes(Integer.toString(number_categories) + '\n'); //Ritorno il numero di characters per quell'utente


                        image = null;

                        rs = stmt.executeQuery("SELECT * from Categorie c WHERE c.id_sessione = " + session_id);
                        while (rs.next()) {

                            sendMessage("N:" + rs.getString("nome"));
                            //dos.writeBytes(rs.getString("nome") + '\n');

                            sendMessage("D:" + rs.getString("descrizione"));
                            //dos.writeBytes(rs.getString("descrizione") + '\n');


                            sendMessage("TP:" + rs.getString("tipo_potenza"));
                            //dos.writeBytes(rs.getString("tipo_potenza") + '\n');

                            image = rs.getBlob("foto");
                            byte barr[] = image.getBytes(1,(int)image.length());
                            dos.write(barr);

                            number_object_categories = 0;

                            rs_temp = stmt.executeQuery("SELECT * from Oggetti o WHERE o.id_categoria = " + rs.getString("id") + " AND o.id_sessione = " + session_id);
                            while (rs_temp.next()) {
                                number_object_categories++;
                            }

                            sendMessage("NC:" + Integer.toString(number_object_categories));
                            //dos.writeBytes(Integer.toString(number_object_categories) + '\n'); //Restituisco il numero di oggetti per categoria

                        }



                    }
                    else
                        sendMessage("-1");
                        //dos.writeBytes("-1" + '\n');
                }


                else if (received.equals("show_category_all"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true) {


                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        number_categories = 0;

                        rs = stmt.executeQuery("SELECT * from Categorie c WHERE c.id_sessione = " + session_id);
                        while (rs.next()) {

                            number_categories++;

                        }

                        sendMessage(Integer.toString(number_categories));
                        //dos.writeBytes(Integer.toString(number_categories) + '\n'); //Ritorno il numero di characters per quell'utente


                        image = null;

                        rs = stmt.executeQuery("SELECT * from Categorie c WHERE c.id_sessione = " + session_id);
                        while (rs.next()) {
                            sendMessage("N:" + rs.getString("nome"));
                            //dos.writeBytes(rs.getString("nome") + '\n');

                            sendMessage("D:" + rs.getString("descrizione"));
                            //dos.writeBytes(rs.getString("descrizione") + '\n');


                            sendMessage("TP:" + rs.getString("tipo_potenza"));
                            //dos.writeBytes(rs.getString("tipo_potenza") + '\n');

                            image = rs.getBlob("foto");
                            byte barr[] = image.getBytes(1,(int)image.length());
                            dos.write(barr);

                            number_object_categories = 0;

                            rs_temp = stmt.executeQuery("SELECT * from Oggetti o WHERE o.id_categoria = " + rs.getString("id") + " AND o.id_sessione = " + session_id);
                            while (rs_temp.next()) {
                                number_object_categories++;
                            }
                            sendMessage("NC:" + Integer.toString(number_object_categories));
                            //dos.writeBytes(Integer.toString(number_object_categories) + '\n'); //Restituisco il numero di oggetti per categoria



                        }



                    }
                    else
                        sendMessage("-1");
                        //dos.writeBytes("-1" + '\n');
                }

                else if (received.equals("show_category_objects"))
                {
                    c_name = dis.readLine(); //Mi prendo il nome della categoria


                    if(is_disconnect == false && is_connect == true && is_session_connected == true) {


                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma



                        find_category = false;

                        rs = stmt.executeQuery("SELECT * from Category c WHERE c.nome = '" + c_name + "' AND c.id_sessione = " + session_id);
                        while (rs.next()) {
                            find_category = true;
                            category_id = rs.getInt("id");
                        }


                        if(find_category == false)
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }
                        else
                        {
                            rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN Categorie c ON o.id_categoria = c.id  WHERE c.id = " + category_id);
                            while (rs.next()) {
                                number_object_categories++;
                            }


                            sendMessage(Integer.toString(number_object_categories));
                            //dos.writeBytes(Integer.toString(number_object_categories) + '\n'); // Restituisco il numero di oggetti del backpack

                            image = null;

                            rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN Categorie c ON o.id_categoria = c.id  WHERE c.id = " + category_id);
                            while (rs.next()) {

                                sendMessage("N:" + rs.getString("nome"));
                                //dos.writeBytes(rs.getString("nome") + '\n'); // Restituisco il nome dell'oggetto

                                sendMessage("D:" + rs.getString("descrizione"));
                                //dos.writeBytes(rs.getString("descrizione") + '\n'); // Restituisco la descrizione dell'oggetto

                                image = rs.getBlob("foto");
                                byte barr[] = image.getBytes(1,(int)image.length());
                                sleep(150);
                                dos.write(barr);

                                //dos.writeBytes( + '\n'); // Restituisco la foto dell'oggetto

                                sendMessage("V:" + rs.getString("valore"));
                                //dos.writeBytes(rs.getString("valore") + '\n'); // Restituisco il valore dell'oggetto

                                sendMessage("Q:" + rs.getString("quantita"));
                                //dos.writeBytes(rs.getString("quantita") + '\n'); // Restituisco la quantita dell'oggetto
                            }

                        }

                    }
                    else
                        sendMessage("-1");
                        //dos.writeBytes("-1" + '\n');
                }


                else if (received.equals("show_user_character"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == false) {

                        sendMessage("1");
                        //dos.writeBytes("1" + '\n');

                        is_host = false;
                        rs_temp = stmt.executeQuery("SELECT * FROM Personaggi p WHERE p.id_sessione = " + session_id + " AND p.id_utente = " + client_id + " AND p.is_character = 1");
                        while (rs_temp.next()) {
                            number_characters++;
                        }

                        //sendMessage("NC:" + Integer.toString(number_characters));
                        sendMessage(Integer.toString(number_characters));
                        //dos.writeBytes(Integer.toString(number_characters) + '\n'); //Ritorno il numero di characters per quell'utente

                        image = null;

                        rs_temp = stmt.executeQuery("SELECT * FROM Personaggi p WHERE p.id_sessione = " + session_id + " AND p.id_utente = " + client_id + " AND p.is_character = 1");
                        while (rs_temp.next()) {

                            sendMessage("N:" + rs_temp.getString("nome"));
                            //dos.writeBytes(rs_temp.getString("nome") + '\n');

                            sendMessage("HP:" + Integer.toString(rs_temp.getInt("hp")));
                            //dos.writeBytes(Integer.toString(rs_temp.getInt("hp")) + '\n');

                            sendMessage("HPMAX:" + Integer.toString(rs_temp.getInt("hp_max")));
                            //dos.writeBytes(Integer.toString(rs_temp.getInt("hp_max")) + '\n');

                            image = rs.getBlob("immagine");
                            byte barr[] = image.getBytes(1,(int)image.length());
                            dos.write(barr);
                        }


                    }
                    else
                        sendMessage("-1");
                        //dos.writeBytes("-1" + '\n');
                }



                //TRADING
                else if (received.equals("trading_with"))
                {

                    //Mi prendo i dati
                    trade_name_c = dis.readLine();

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


                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {

                        is_trading_enabled1 = false;
                        is_trading_enabled2 = false;

                        sendMessage("1");
                        //dos.writeBytes("1" + '\n');



                        find_trade = false;

                        rs_temp = stmt.executeQuery("SELECT * FROM Personaggi p WHERE p.id_sessione = " + session_id + " AND p.id = " + character_id + " AND p.is_character = 1");
                        while (rs_temp.next()) {

                            if(rs_temp.getInt("is_trading_enabled") == 0)
                            {
                                is_trading_enabled1 = false;
                            }
                            else
                            {
                                is_trading_enabled1 = true;
                            }
                        }


                        rs_temp = stmt.executeQuery("SELECT * FROM Personaggi p WHERE p.id_sessione = " + session_id + " AND p.nome = '" + trade_name_c + "' AND p.is_character = 1");
                        while (rs_temp.next()) {
                            find_trade = true;
                            send_id = rs.getInt("id");

                            if(rs_temp.getInt("is_trading_enabled") == 0)
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
                            sendMessage("1");
                            //dos.writeBytes("1" + '\n');




                            is_o1_ok = true;
                            is_o2_ok = true;

                            for(i = 0; i < number_item_trade_1; i++)
                            {
                                find_trade = false;
                                rs_temp = stmt.executeQuery("SELECT * FROM R_Personaggio_Oggetto rpo INNER JOIN Oggetti o on rpo.id_oggetto = o.id WHERE rpo.id_personaggio = " + character_id + " AND o.nome = '" + trade_o_1[i] + "' AND rpo.quantita <= " + trade_q_1[i]);
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
                                rs_temp = stmt.executeQuery("SELECT * FROM R_Personaggio_Oggetto rpo INNER JOIN Oggetti o on rpo.id_oggetto = o.id WHERE rpo.id_personaggio = " + send_id + " AND o.nome = '" + trade_o_2[i] + "' AND rpo.quantita <= " + trade_q_2[i]);
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
                                        pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET quantita = " + x + " WHERE id_personaggio = " + character_id + " AND id_oggetto = " + id_items_1[i]);
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
                                        pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET quantita = " + x + " WHERE id_personaggio = " + send_id + " AND id_oggetto = " + id_items_2[i]);
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

                                            pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET quantita = " + x + " WHERE id_personaggio = " + character_id + " AND id_oggetto = " + id_items_2[i]);
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

                                            pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET quantita = " + x + " WHERE id_personaggio = " + send_id + " AND id_oggetto = " + id_items_1[i]);
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


                                        sendMessage("1");
                                        //dos.writeBytes("1" + '\n');


                                    }
                                }
                                else
                                {
                                    sendMessage("-4");
                                    //dos.writeBytes("-4" + '\n');
                                }


                            }



                            else
                            {
                                sendMessage("-3");
                                //dos.writeBytes("-3" + '\n');
                            }

                        }
                        else
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }


                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
                }

                //TRADING FROM
                else if (received.equals("trading_from"))
                {
                    //Mi prendo i dati e poi li elaboro
                    trade_c_name1 = dis.readLine();
                    trade_c_name2 = dis.readLine();

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


                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {

                        is_trading_enabled1 = false;
                        is_trading_enabled2 = false;

                        sendMessage("1");
                        //dos.writeBytes("1" + '\n');



                        find_trade1 = false;
                        find_trade2 = false;

                        rs_temp = stmt.executeQuery("SELECT * FROM Personaggi p WHERE p.id_sessione = " + session_id + " AND p.nome = '" + trade_c_name1 + "' AND p.is_character = 1");
                        while (rs_temp.next()) {

                            find_trade1 = true;


                            if(rs_temp.getInt("is_trading_enabled") == 0)
                            {
                                is_trading_enabled1 = false;
                            }
                            else
                            {
                                is_trading_enabled1 = true;
                            }
                        }


                        rs_temp = stmt.executeQuery("SELECT * FROM Personaggi p WHERE p.id_sessione = " + session_id + " AND p.nome = '" + trade_c_name2 + "' AND p.is_character = 1");
                        while (rs_temp.next()) {
                            find_trade2 = true;
                            send_id = rs.getInt("id");

                            if(rs_temp.getInt("is_trading_enabled") == 0)
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
                            sendMessage("1");
                            //dos.writeBytes("1" + '\n');


                            is_o1_ok = true;
                            is_o2_ok = true;

                            for(i = 0; i < number_item_trade_1; i++)
                            {
                                find_trade = false;
                                rs_temp = stmt.executeQuery("SELECT * FROM R_Personaggio_Oggetto rpo INNER JOIN Oggetti o on rpo.id_oggetto = o.id WHERE rpo.id_personaggio = " + trade_c_id1 + " AND o.nome = '" + trade_o_1[i] + "' AND rpo.quantita <= " + trade_q_1[i]);
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
                                rs_temp = stmt.executeQuery("SELECT * FROM R_Personaggio_Oggetto rpo INNER JOIN Oggetti o on rpo.id_oggetto = o.id WHERE rpo.id_personaggio = " + trade_c_id2 + " AND o.nome = '" + trade_o_2[i] + "' AND rpo.quantita <= " + trade_q_2[i]);
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
                                        pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET quantita = " + x + " WHERE id_personaggio = " + trade_c_id1 + " AND id_oggetto = " + id_items_1[i]);
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
                                        pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET quantita = " + x + " WHERE id_personaggio = " + trade_c_id2 + " AND id_oggetto = " + id_items_2[i]);
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

                                            pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET quantita = " + x + " WHERE id_personaggio = " + trade_c_id1 + " AND id_oggetto = " + id_items_2[i]);
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

                                            pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET quantita = " + x + " WHERE id_personaggio = " + trade_c_id2 + " AND id_oggetto = " + id_items_1[i]);
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


                                        sendMessage("1");
                                        //dos.writeBytes("1" + '\n');


                                    }
                                }
                                else
                                {
                                    sendMessage("-4");
                                    //dos.writeBytes("-4" + '\n');
                                }


                            }



                            else
                            {
                                sendMessage("-3");
                                //dos.writeBytes("-3" + '\n');
                            }

                        }
                        else
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }


                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
                }

                //Unlock trading
                else if (received.equals("unlock_trading"))
                {
                    character_nome = dis.readLine();

                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true)
                    {
                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;


                        rs = stmt.executeQuery("SELECT * from Personaggi p WHERE p.id_sessione = " + session_id + " AND p.nome = '" + character_nome + "'");
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

                            sendMessage("1");
                            //dos.writeBytes("1" + '\n');

                        }
                        else
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        sendMessage("-1");
                        //dos.writeBytes("-1" + '\n');
                    }
                }

                //Lock trading
                else if (received.equals("lock_trading"))
                {
                    character_nome = dis.readLine();

                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true)
                    {
                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;

                        rs = stmt.executeQuery("SELECT * from Personaggi p WHERE p.id_sessione = " + session_id + " AND p.nome = '" + character_nome + "'");
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

                            sendMessage("1");
                            //dos.writeBytes("1" + '\n');

                        }
                        else
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        sendMessage("-1");
                        //dos.writeBytes("-1" + '\n');
                    }
                }




                //CHARACTER E PERSONAGGIO

                //Creating new character
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
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            i++;

                        }

                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
                }

                else if (received.equals("disconnect_character"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {
                        character_id = 0;
                        is_character_connected = false;

                        sendMessage("1");
                        //dos.writeBytes("1" + '\n');

                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
                }

                else if (received.equals("add_character"))
                {

                    character_nome = dis.readLine();
                    character_hp = Integer.parseInt(dis.readLine());
                    character_biografia = dis.readLine();
                    character_razza = dis.readLine();
                    character_eta = Integer.parseInt(dis.readLine());
                    character_peso = Float.parseFloat(dis.readLine());
                    character_altezza = Float.parseFloat(dis.readLine());
                    character_hp_max = Integer.parseInt(dis.readLine());

                    character_capelli = dis.readLine();
                    character_occhi = dis.readLine();
                    character_mark = dis.readLine();
                    character_cicatrici = dis.readLine();
                    character_tatuaggi = dis.readLine();
                    character_luogo_nascita = dis.readLine();
                    character_sesso = dis.readLine();
                    character_colore_pelle = dis.readLine();
                    character_anno_nascita = Integer.parseInt(dis.readLine());
                    character_mese_nascita = Integer.parseInt(dis.readLine());
                    character_giorno_nascita = Integer.parseInt(dis.readLine());
                    character_classe = dis.readLine();



                    if(is_disconnect == false && is_connect == true && is_session_connected == true) {


                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;



                        rs = stmt.executeQuery("SELECT * from Personaggi p WHERE p.is_character = 1 AND p.id_sessione = " + session_id);
                        while (rs.next()) {

                            if(character_nome.equals(rs.getString("nome")))
                                find_category = true;

                        }

                        if(find_category)
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }

                        else
                        {
                            pstmt = conn.prepareStatement("INSERT INTO Personaggi " + "(nome, hp, biografia, razza, eta, peso, altezza, hp_max, id_sessione, id_utente, is_character, colore_capelli, colore_occhi, distinguersi_mark, cicatrici, tatuaggi, anno_nascita, luogo_nascita, sesso, colore_pelle, classe, mese_nascita, giorno_nascita /*Immagine*/ ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");


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

                            pstmt.setString(12, character_capelli);
                            pstmt.setString(13, character_occhi);
                            pstmt.setString(14, character_mark);
                            pstmt.setString(15, character_cicatrici);
                            pstmt.setString(16, character_tatuaggi);
                            pstmt.setInt(17, character_anno_nascita);
                            pstmt.setString(18, character_luogo_nascita);
                            pstmt.setString(19, character_sesso);
                            pstmt.setString(20, character_colore_pelle);

                            pstmt.setString(21, character_classe);

                            pstmt.setInt(22, character_mese_nascita);
                            pstmt.setInt(23, character_giorno_nascita);

                            //Inserimento foto

                            pstmt.execute();
                            pstmt.close(); // rilascio le risorse

                            sendMessage("1");
                            //dos.writeBytes("1" + '\n');
                        }

                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
                }

                //Creating new personaggio
                else if (received.equals("add_personaggio"))
                {

                    character_nome = dis.readLine();
                    character_hp = Integer.parseInt(dis.readLine());
                    character_biografia = dis.readLine();
                    character_razza = dis.readLine();
                    character_eta = Integer.parseInt(dis.readLine());
                    character_peso = Float.parseFloat(dis.readLine());
                    character_altezza = Float.parseFloat(dis.readLine());
                    character_hp_max = Integer.parseInt(dis.readLine());


                    what_character = dis.readLine(); // Tipo di character

                    character_capelli = dis.readLine();
                    character_occhi = dis.readLine();
                    character_mark = dis.readLine();
                    character_cicatrici = dis.readLine();
                    character_tatuaggi = dis.readLine();
                    character_luogo_nascita = dis.readLine();
                    character_sesso = dis.readLine();
                    character_colore_pelle = dis.readLine();
                    character_anno_nascita = Integer.parseInt(dis.readLine());
                    character_mese_nascita = Integer.parseInt(dis.readLine());
                    character_giorno_nascita = Integer.parseInt(dis.readLine());
                    character_classe = dis.readLine();

                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true) {


                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;



                        rs = stmt.executeQuery("SELECT * from Personaggi p WHERE p.id_sessione = " + session_id);
                        while (rs.next()) {

                            if(character_nome.equals(rs.getString("nome")))
                                find_category = true;

                        }

                        if(find_category)
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }

                        else
                        {
                            pstmt = conn.prepareStatement("INSERT INTO Personaggi " + "(nome, hp, biografia, razza, eta, peso, altezza, hp_max, id_sessione, id_utente, is_character, is_npc, is_riding_animal, is_enemy,  colore_capelli, colore_occhi, distinguersi_mark, cicatrici, tatuaggi, anno_nascita, luogo_nascita, sesso, colore_pelle, classe, mese_nascita, giorno_nascita /*Immagine*/ ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");


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

                            pstmt.setString(15, character_capelli);
                            pstmt.setString(16, character_occhi);
                            pstmt.setString(17, character_mark);
                            pstmt.setString(18, character_cicatrici);
                            pstmt.setString(19, character_tatuaggi);
                            pstmt.setInt(20, character_anno_nascita);
                            pstmt.setString(21, character_luogo_nascita);
                            pstmt.setString(22, character_sesso);
                            pstmt.setString(23, character_colore_pelle);
                            pstmt.setString(24, character_classe);

                            pstmt.setInt(25, character_mese_nascita);
                            pstmt.setInt(26, character_giorno_nascita);

                            pstmt.execute();
                            pstmt.close(); // rilascio le risorse

                            sendMessage(variable_return);
                            //dos.writeBytes(variable_return + '\n');
                        }

                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
                }

                else if (received.equals("get_personaggi_data"))
                {
                    o_nome = dis.readLine();
                    ob_type = dis.readLine();


                    if(is_disconnect == false && is_connect == true && is_session_connected == true)
                    {
                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;


                        rs = stmt.executeQuery("SELECT * from Personaggi p WHERE p.id_sessione = " + session_id + " AND p.nome = '" + o_nome + "'");
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
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("hp")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("hp")) + '\n');
                                }

                                else if(ob_type.equals("biografia"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(rs.getString("biografia"));
                                    //dos.writeBytes(rs.getString("biografia") + '\n');
                                }

                                else if(ob_type.equals("razza"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(rs.getString("razza"));
                                    //dos.writeBytes(rs.getString("razza") + '\n');
                                }

                                else if(ob_type.equals("eta"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("eta")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("eta")) + '\n');
                                }

                                else if(ob_type.equals("peso"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Float.toString(rs.getFloat("peso")));
                                    //dos.writeBytes( Float.toString(rs.getFloat("peso")) + '\n');
                                }

                                else if(ob_type.equals("altezza"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Float.toString(rs.getFloat("altezza")));
                                    //dos.writeBytes( Float.toString(rs.getFloat("altezza")) + '\n');
                                }

                                //GET FOTO


                                else if(ob_type.equals("hp_max"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("hp_max")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("hp_max")) + '\n');
                                }

                                else if(ob_type.equals("is_character"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("is_character")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("is_character")) + '\n');
                                }

                                else if(ob_type.equals("is_npc"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("is_npc")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("is_npc")) + '\n');
                                }

                                else if(ob_type.equals("is_riding_animal"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("is_riding_animal")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("is_riding_animal")) + '\n');
                                }


                                else if(ob_type.equals("is_enemy"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("is_enemy")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("is_enemy")) + '\n');
                                }


                                else if(ob_type.equals("punti_skill"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("punti_skill")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("punti_skill")) + '\n');
                                }

                                else if(ob_type.equals("is_enabled"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("is_enabled")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("is_enabled")) + '\n');
                                }

                                else if(ob_type.equals("is_trading_enabled"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("is_trading_enabled")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("is_trading_enabled")) + '\n');
                                }

                                else if(ob_type.equals("colore_capelli"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(rs.getString("colore_capelli"));
                                    //dos.writeBytes(rs.getString("colore_capelli") + '\n');
                                }

                                else if(ob_type.equals("distinguersi_mark"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(rs.getString("distinguersi_mark"));
                                    //dos.writeBytes(rs.getString("distinguersi_mark") + '\n');
                                }

                                else if(ob_type.equals("cicatrici"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(rs.getString("cicatrici"));
                                    //dos.writeBytes(rs.getString("cicatrici") + '\n');
                                }

                                else if(ob_type.equals("tatuaggi"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(rs.getString("tatuaggi"));
                                    //dos.writeBytes(rs.getString("tatuaggi") + '\n');
                                }

                                else if(ob_type.equals("anno_nascita"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("anno_nascita")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("anno_nascita")) + '\n');
                                }

                                else if(ob_type.equals("mese_nascita"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("mese_nascita")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("anno_nascita")) + '\n');
                                }

                                else if(ob_type.equals("giorno_nascita"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("giorno_nascita")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("anno_nascita")) + '\n');
                                }

                                else if(ob_type.equals("luogo_nascita"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(rs.getString("luogo_nascita"));
                                    //dos.writeBytes(rs.getString("luogo_nascita") + '\n');
                                }

                                else if(ob_type.equals("sesso"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(rs.getString("sesso"));
                                    //dos.writeBytes(rs.getString("sesso") + '\n');
                                }

                                else if(ob_type.equals("colore_pelle"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(rs.getString("colore_pelle"));
                                    //dos.writeBytes(rs.getString("colore_pelle") + '\n');
                                }

                                else if(ob_type.equals("slot_head"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("slot_head")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("slot_head")) + '\n');
                                }

                                else if(ob_type.equals("slot_torso"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("slot_torso")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("slot_torso")) + '\n');
                                }

                                else if(ob_type.equals("slot_arms"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("slot_arms")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("slot_arms")) + '\n');
                                }

                                else if(ob_type.equals("slot_legs"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("slot_legs")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("slot_legs")) + '\n');
                                }

                                else if(ob_type.equals("slot_weapons"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("slot_weapons")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("slot_weapons")) + '\n');
                                }

                                else if(ob_type.equals("slot_gloves"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("slot_gloves")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("slot_gloves")) + '\n');
                                }

                                else if(ob_type.equals("slot_shoes"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("slot_shoes")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("slot_shoes")) + '\n');
                                }

                                else if(ob_type.equals("slot_greaves"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("slot_greaves")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("slot_greaves")) + '\n');
                                }

                                else if(ob_type.equals("slot_magic"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("slot_magic")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("slot_magic")) + '\n');
                                }

                                else if(ob_type.equals("slot_other"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("slot_other")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("slot_other")) + '\n');
                                }

                                else if(ob_type.equals("slot_ammo"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("slot_ammo")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("slot_ammo")) + '\n');
                                }

                                else if(ob_type.equals("slot_shield"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("slot_shield")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("slot_ammo")) + '\n');
                                }

                                else if(ob_type.equals("peso_max"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');
                                    sendMessage(Integer.toString(rs.getInt("peso_max")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("slot_ammo")) + '\n');
                                }

                                else
                                {
                                    sendMessage("-3");
                                    //dos.writeBytes("-3" + '\n');
                                }
                            }



                        }
                        else
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        sendMessage("-1");
                        //dos.writeBytes("-1" + '\n');
                    }
                }

                else if (received.equals("update_personaggi_data"))
                {
                    o_nome = dis.readLine();
                    ob_type = dis.readLine();
                    ob_data = dis.readLine();



                    if(is_disconnect == false && is_connect == true && is_session_connected == true)
                    {
                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;



                        rs = stmt.executeQuery("SELECT * from Personaggi p WHERE p.id_sessione = " + session_id + " AND p.nome = '" + o_nome + "'");
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
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET nome = '" + ob_data + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("hp"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET hp = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("biografia"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET biografia = '" + ob_data + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("razza"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET razza = '" + ob_data + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("eta"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET eta = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("peso"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET peso = '" + Float.parseFloat(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("altezza"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET altezza = '" + Float.parseFloat(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            //UPDATE FOTO




                            //GAME MASTER ADD ON

                            else if(ob_type.equals("hp_max"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET hp_max = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("is_character"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET is_character = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("is_npc"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET is_npc = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("is_riding_animal"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET is_riding_animal = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }


                            else if(ob_type.equals("is_enemy"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET is_enemy = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }


                            else if(ob_type.equals("punti_skill"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET punti_skill = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("is_enabled"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET is_enabled = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }


                            else if(ob_type.equals("colore_capelli"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET colore_capelli = '" + ob_data + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("colore_occhi"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET colore_occhi = '" + ob_data + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("distinguersi_mark"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET distinguersi_mark = '" + ob_data + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("cicatrici"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET cicatrici = '" + ob_data + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("tatuaggi"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET tatuaggi = '" + ob_data + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("anno_nascita"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET anno_nascita = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("mese_nascita"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET mese_nascita = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("giorno_nascita"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET giorno_nascita = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("luogo_nascita"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET luogo_nascita = '" + ob_data + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("sesso"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET sesso = '" + ob_data + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("colore_pelle"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET colore_pelle = '" + ob_data + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("slot_head"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET slot_head = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("slot_torso"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET slot_torso = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("slot_arms"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET slot_arms = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("slot_legs"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET slot_legs = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("slot_weapons"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET slot_weapons = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("slot_gloves"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET slot_gloves = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("slot_shoes"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET slot_shoes = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("slot_greaves"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET slot_greaves = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("slot_magic"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET slot_magic = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("slot_other"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET slot_other = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("slot_ammo"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET slot_ammo = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("slot_shield"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET slot_shield = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("peso_max"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Personaggi SET peso_max = '" + Integer.parseInt(ob_data) + "' where Personaggi.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else
                            {
                                sendMessage("-3");
                                //dos.writeBytes("-3" + '\n');
                            }

                        }
                        else
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        sendMessage("-1");
                        //dos.writeBytes("-1" + '\n');
                    }
                }

                //Update hp of character
                else if (received.equals("update_hp"))
                {
                    character_nome = dis.readLine();
                    character_hp = Integer.parseInt(dis.readLine());

                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true)
                    {
                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;

                        rs = stmt.executeQuery("SELECT * from Personaggi p WHERE p.id_sessione = " + session_id + " AND p.nome = '" + character_nome + "'");
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

                            sendMessage("1");
                            //dos.writeBytes("1" + '\n');

                        }
                        else
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        sendMessage("-1");
                        //dos.writeBytes("-1" + '\n');
                    }
                }

                //Update hp_max of character
                else if (received.equals("update_hp_max"))
                {
                    character_nome = dis.readLine();
                    character_hp_max = Integer.parseInt(dis.readLine());

                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true)
                    {
                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;


                        rs = stmt.executeQuery("SELECT * from Personaggi p WHERE p.id_sessione = " + session_id + " AND p.nome = '" + character_nome + "'");
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

                            sendMessage("1");
                            //dos.writeBytes("1" + '\n');

                        }
                        else
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        sendMessage("-1");
                        //dos.writeBytes("-1" + '\n');
                    }
                }

                else if (received.equals("get_peso"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true) {
                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        ch_peso = 0;

                        find_category = false;

                        rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN R_Personaggio_Oggetto rpo on rpo.id_oggetto = o.id INNER JOIN Personaggi p on p.id = rpo.id_personaggio WHERE o.id_sessione = " + session_id + " AND p.id = '" + character_id + "'");
                        while (rs.next()) {
                            find_category = true;

                            ch_peso = ch_peso + (rs.getFloat("peso") * rs.getInt("quantita"));
                        }

                        if (find_category == true) {
                            sendMessage(Float.toString(ch_peso));
                        }
                        else
                        {
                            sendMessage("-2");
                        }
                    }
                    else
                        sendMessage("-1");

                }

                else if (received.equals("get_backpack"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {


                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        number_items_b = 0;

                        rs = stmt.executeQuery("SELECT * from R_Personaggio_Oggetto rpo WHERE rpo.id_personaggio = " + character_id);
                        while (rs.next()) {

                            number_items_b++;

                        }

                        sendMessage(Integer.toString(number_items_b));
                        //dos.writeBytes(Integer.toString(number_items_b) + '\n'); // Restituisco il numero di oggetti del backpack

                        image = null;

                        rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN R_Personaggio_Oggetto rpo on rpo.id_oggetto = o.id WHERE rpo.id_personaggio = " + character_id);
                        while (rs.next()) {

                            sendMessage("N:" + rs.getString("nome"));
                            //dos.writeBytes(rs.getString("nome") + '\n'); // Restituisco il nome dell'oggetto

                            sendMessage("D:" + rs.getString("descrizione"));
                            //dos.writeBytes(rs.getString("descrizione") + '\n'); // Restituisco la descrizione dell'oggetto

                            image = rs.getBlob("foto");
                            byte barr[] = image.getBytes(1,(int)image.length());
                            sleep(150);
                            dos.write(barr);

                            //dos.writeBytes( + '\n'); // Restituisco la foto dell'oggetto

                            sendMessage("V:" + rs.getString("valore"));
                            //dos.writeBytes(rs.getString("valore") + '\n'); // Restituisco il valore dell'oggetto

                            sendMessage("Q:" + rs.getString("quantita"));
                            //dos.writeBytes(rs.getString("quantita") + '\n'); // Restituisco la quantita dell'oggetto



                            sendMessage("RC:" + rs.getString("rarita_colore")); //Mando la rarita del colore

                            sendMessage("PO:" + Float.toString(rs.getFloat("potenza"))); //Mando la potenza

                            sendMessage("PE:" + Float.toString(rs.getFloat("peso"))); //Mando il peso

                            sendMessage("DUR:" + Integer.toString(rs.getInt("durabilita"))); //Mando la potenza


                        }


                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
                }

                else if (received.equals("get_user_backpack")) // DA CAMBIARE
                {
                    filter_item = dis.readLine(); //Mi prendo l'id dell'utente

                    send_id = Integer.parseInt(filter_item);

                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {


                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma


                        number_items_b = 0;

                        rs = stmt.executeQuery("SELECT * from R_Personaggio_Oggetto rpo WHERE rpo.id_personaggio = " + send_id);
                        while (rs.next()) {

                            number_items_b++;

                        }

                        sendMessage(Integer.toString(number_items_b));
                        //dos.writeBytes(Integer.toString(number_items_b) + '\n'); // Restituisco il numero di oggetti del backpack

                        image = null;

                        rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN R_Personaggio_Oggetto rpo on rpo.id_oggetto = o.id WHERE rpo.id_personaggio = " + send_id);
                        while (rs.next()) {

                            sendMessage("N:" + rs.getString("nome"));
                            //dos.writeBytes(rs.getString("nome") + '\n'); // Restituisco il nome dell'oggetto

                            sendMessage("D:" + rs.getString("descrizione"));
                            //dos.writeBytes(rs.getString("descrizione") + '\n'); // Restituisco la descrizione dell'oggetto

                            image = rs.getBlob("foto");
                            byte barr[] = image.getBytes(1,(int)image.length());
                            sleep(150);
                            dos.write(barr);

                            //dos.writeBytes( + '\n'); // Restituisco la foto dell'oggetto

                            sendMessage("V:" + rs.getString("valore"));
                            //dos.writeBytes(rs.getString("valore") + '\n'); // Restituisco il valore dell'oggetto

                            sendMessage("Q:" + rs.getString("quantita"));
                            //dos.writeBytes(rs.getString("quantita") + '\n'); // Restituisco la quantita dell'oggetto

                            sendMessage("RC:" + rs.getString("rarita_colore")); //Mando la rarita del colore

                            sendMessage("PO:" + Float.toString(rs.getFloat("potenza"))); //Mando la potenza

                            sendMessage("PE:" + Float.toString(rs.getFloat("peso"))); //Mando il peso

                            sendMessage("DUR:" + Integer.toString(rs.getInt("durabilita"))); //Mando la potenza
                        }


                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
                }

                else if (received.equals("get_character_backpack"))
                {
                    filter_item = dis.readLine(); //Mi prendo il nome del character

                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {


                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma



                        find_character = false;

                        rs = stmt.executeQuery("SELECT * from Personaggi p WHERE p.nome = '" + filter_item + "' AND p.is_character = 1 AND p.id_sessione = " + session_id);
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

                            sendMessage(Integer.toString(number_items_b));
                            //dos.writeBytes(Integer.toString(number_items_b) + '\n'); // Restituisco il numero di oggetti del backpack

                            image = null;

                            rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN R_Personaggio_Oggetto rpo on rpo.id_oggetto = o.id WHERE rpo.id_personaggio = " + send_id);
                            while (rs.next()) {

                                sendMessage("N:" + rs.getString("nome"));
                                //dos.writeBytes(rs.getString("nome") + '\n'); // Restituisco il nome dell'oggetto

                                sendMessage("D:" + rs.getString("descrizione"));
                                //dos.writeBytes(rs.getString("descrizione") + '\n'); // Restituisco la descrizione dell'oggetto

                                image = rs.getBlob("foto");
                                byte barr[] = image.getBytes(1,(int)image.length());
                                sleep(150);
                                dos.write(barr);

                                //dos.writeBytes( + '\n'); // Restituisco la foto dell'oggetto

                                sendMessage("V:" + rs.getString("valore"));
                                //dos.writeBytes(rs.getString("valore") + '\n'); // Restituisco il valore dell'oggetto

                                sendMessage("Q:" + rs.getString("quantita"));
                                //dos.writeBytes(rs.getString("quantita") + '\n'); // Restituisco la quantita dell'oggetto
                            }
                        }
                        else
                            sendMessage("-2");
                        //dos.writeBytes("-2" + '\n');

                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
                }

                else if (received.equals("get_filtered_backpack"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {

                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        number_items_b = 0;

                        filter_item = dis.readLine(); //Mi prendo il filtro

                        rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN R_Personaggio_Oggetto rpo on rpo.id_oggetto = o.id INNER JOIN Categorie c ON c.id = o.id_categoria WHERE rpo.id_personaggio = " + character_id + " AND c.nome = '" + filter_item + "'");
                        while (rs.next()) {

                            number_items_b++;

                        }

                        sendMessage(Integer.toString(number_items_b));
                        //dos.writeBytes(Integer.toString(number_items_b) + '\n'); // Restituisco il numero di oggetti del backpack

                        image = null;

                        rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN R_Personaggio_Oggetto rpo on rpo.id_oggetto = o.id INNER JOIN Categorie c ON c.id = o.id_categoria WHERE rpo.id_personaggio = " + character_id + " AND c.nome = '" + filter_item + "'");
                        while (rs.next()) {

                            sendMessage("N:" + rs.getString("nome"));
                            //dos.writeBytes(rs.getString("nome") + '\n'); // Restituisco il nome dell'oggetto

                            sendMessage("D:" + rs.getString("descrizione"));
                            //dos.writeBytes(rs.getString("descrizione") + '\n'); // Restituisco la descrizione dell'oggetto

                            image = rs.getBlob("foto");
                            byte barr[] = image.getBytes(1,(int)image.length());
                            sleep(150);
                            dos.write(barr);

                            //dos.writeBytes( + '\n'); // Restituisco la foto dell'oggetto

                            sendMessage("V:" + rs.getString("valore"));
                            //dos.writeBytes(rs.getString("valore") + '\n'); // Restituisco il valore dell'oggetto

                            sendMessage("Q:" + rs.getString("quantita"));
                            //dos.writeBytes(rs.getString("quantita") + '\n'); // Restituisco la quantita dell'oggetto


                            sendMessage("RC:" + rs.getString("rarita_colore")); //Mando la rarita del colore

                            sendMessage("PO:" + Float.toString(rs.getFloat("potenza"))); //Mando la potenza

                            sendMessage("PE:" + Float.toString(rs.getFloat("peso"))); //Mando il peso

                            sendMessage("DUR:" + Integer.toString(rs.getInt("durabilita"))); //Mando la potenza


                        }


                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
                }

                //Get quantity of object per character
                //restituisce 1 perchè è entrato poi 1 se ha trovato l'oggetto sul character e lo restituisce poi -2 se non ha trovato l'oggetto e -1 se c'è errore
                else if (received.equals("get_quantity_object"))
                {
                    trade_name_c = dis.readLine();

                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {

                        sendMessage("1");
                        //dos.writeBytes("1" + '\n');



                        find_trade = false;
                        rs_temp = stmt.executeQuery("SELECT * FROM R_Personaggio_Oggetto rpo INNER JOIN Oggetti o on rpo.id_oggetto = o.id WHERE rpo.id_personaggio = " + character_id + " AND o.nome = '" + trade_name_c + "'");
                        while (rs_temp.next()) {
                            find_trade = true;
                            quantity_items_1[0] = rs_temp.getInt("quantita");
                        }

                        if(find_trade)
                        {
                            sendMessage("1");
                            //dos.writeBytes("1" + '\n');

                            sendMessage("QI:" + Integer.toString(quantity_items_1[0]));
                            //dos.writeBytes(Integer.toString(quantity_items_1[0]));
                        }
                        else
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
                }


                //Get quantity of object per un determinato character
                //restituisce 1 perchè è entrato poi 1 se ha trovato l'oggetto sul character e lo restituisce poi -2 se non ha trovato l'oggetto e -1 se c'è errore
                else if (received.equals("get_quantity_object_from_character"))
                {
                    c_name = dis.readLine();
                    trade_name_c = dis.readLine();

                    if(is_disconnect == false && is_connect == true && is_session_connected == true) {

                        sendMessage("1");
                        //dos.writeBytes("1" + '\n');




                        find_trade = false;
                        rs_temp = stmt.executeQuery("SELECT * FROM R_Personaggio_Oggetto rpo INNER JOIN Oggetti o on rpo.id_oggetto = o.id INNER JOIN Personaggi p on p.id = rpo.id_personaggio WHERE p.nome = '" + c_name + "' AND o.nome = '" + trade_name_c + "'");
                        while (rs_temp.next()) {
                            find_trade = true;
                            quantity_items_1[0] = rs_temp.getInt("quantita");
                        }

                        if(find_trade)
                        {
                            sendMessage("1");
                            //dos.writeBytes("1" + '\n');

                            sendMessage("QI:" + Integer.toString(quantity_items_1[0]));
                            //dos.writeBytes(Integer.toString(quantity_items_1[0]));
                        }
                        else
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
                }



                //EQUIPMENT

                else if (received.equals("get_equipment"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {


                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        number_items_b = 0;

                        rs = stmt.executeQuery("SELECT * from R_Personaggio_Oggetto rpo WHERE rpo.id_personaggio = " + character_id + " AND (is_head_eq = 1 OR is_torso_eq = 1 OR is_left_arm_eq = 1 OR is_right_arm_eq = 1 OR is_left_leg_eq = 1 OR is_right_leg_eq = 1 OR is_first_weapon_eq = 1 OR is_secondary_weapon_eq = 1 OR is_gloves_eq = 1 OR is_left_gloves_eq = 1 OR is_right_gloves_eq = 1 OR is_shoes_eq = 1 OR is_greaves_eq = 1 OR is_left_greaves_eq = 1 OR is_right_greaves_eq = 1 OR is_arms_eq = 1 OR is_legs_eq = 1 OR is_ammo_eq = 1 OR is_other_eq = 1 OR is_magic_eq = 1 OR is_shield_eq = 1)");
                        while (rs.next()) {

                            number_items_b++;

                        }

                        sendMessage(Integer.toString(number_items_b));
                        //dos.writeBytes(Integer.toString(number_items_b) + '\n'); // Restituisco il numero di oggetti del backpack

                        image = null;

                        rs = stmt.executeQuery("SELECT * from R_Personaggio_Oggetto rpo WHERE rpo.id_personaggio = " + character_id + " AND (is_head_eq = 1 OR is_torso_eq = 1 OR is_left_arm_eq = 1 OR is_right_arm_eq = 1 OR is_left_leg_eq = 1 OR is_right_leg_eq = 1 OR is_first_weapon_eq = 1 OR is_secondary_weapon_eq = 1 OR is_gloves_eq = 1 OR is_left_gloves_eq = 1 OR is_right_gloves_eq = 1 OR is_shoes_eq = 1 OR is_greaves_eq = 1 OR is_left_greaves_eq = 1 OR is_right_greaves_eq = 1 OR is_arms_eq = 1 OR is_legs_eq = 1 OR is_ammo_eq = 1 OR is_other_eq = 1 OR is_magic_eq = 1 OR is_shield_eq = 1)");
                        while (rs.next()) {

                            if(rs.getInt("is_head_eq") == 1)
                            {
                                sendMessage("head");
                                //dos.writeBytes("head" + '\n');

                            }
                            else if (rs.getInt("is_torso_eq") == 1)
                            {
                                //dos.writeBytes("torso" + '\n');
                                sendMessage("torso");
                            }

                            else if (rs.getInt("is_left_arm_eq") == 1)
                            {
                                //dos.writeBytes("left_arm" + '\n');
                                sendMessage("left_arm");
                            }

                            else if (rs.getInt("is_right_arm_eq") == 1)
                            {
                                //dos.writeBytes("right_arm" + '\n');
                                sendMessage("right_arm");
                            }

                            else if (rs.getInt("is_left_leg_eq") == 1)
                            {
                                //dos.writeBytes("left_leg" + '\n');
                                sendMessage("left_leg");
                            }

                            else if (rs.getInt("is_right_leg_eq") == 1)
                            {
                                //dos.writeBytes("right_leg" + '\n');
                                sendMessage("right_leg");
                            }

                            else if (rs.getInt("is_first_weapon_eq") == 1)
                            {
                                //dos.writeBytes("first_weapon" + '\n');
                                sendMessage("first_weapon");
                            }

                            else if (rs.getInt("is_secondary_weapon_eq") == 1)
                            {
                                //dos.writeBytes("secondary_weapon" + '\n');
                                sendMessage("secondary_weapon");
                            }

                            else if (rs.getInt("is_gloves_eq") == 1)
                            {
                                //dos.writeBytes("gloves" + '\n');
                                sendMessage("gloves");
                            }

                            else if (rs.getInt("is_left_gloves_eq") == 1)
                            {
                                //dos.writeBytes("left_gloves" + '\n');
                                sendMessage("left_gloves");
                            }

                            else if (rs.getInt("is_right_gloves_eq") == 1)
                            {
                                //dos.writeBytes("right_gloves" + '\n');
                                sendMessage("right_gloves");
                            }

                            else if (rs.getInt("is_shoes_eq") == 1)
                            {
                                //dos.writeBytes("shoes" + '\n');
                                sendMessage("shoes");
                            }

                            else if (rs.getInt("is_greaves_eq") == 1)
                            {
                                //dos.writeBytes("greaves" + '\n');
                                sendMessage("greaves");
                            }

                            else if (rs.getInt("is_left_greaves_eq") == 1)
                            {
                                //dos.writeBytes("left_greaves" + '\n');
                                sendMessage("left_greaves");
                            }

                            else if (rs.getInt("is_right_greaves_eq") == 1)
                            {
                                //dos.writeBytes("right_greaves" + '\n');
                                sendMessage("right_greaves");
                            }

                            else if (rs.getInt("is_magic_eq") == 1)
                            {
                                //dos.writeBytes("greaves" + '\n');
                                sendMessage("magic");
                            }

                            else if (rs.getInt("is_other_eq") == 1)
                            {
                                //dos.writeBytes("greaves" + '\n');
                                sendMessage("other");
                            }

                            else if (rs.getInt("is_arms_eq") == 1)
                            {
                                //dos.writeBytes("greaves" + '\n');
                                sendMessage("arms");
                            }

                            else if (rs.getInt("is_legs_eq") == 1)
                            {
                                //dos.writeBytes("greaves" + '\n');
                                sendMessage("legs");
                            }

                            else if (rs.getInt("is_ammo_eq") == 1)
                            {
                                //dos.writeBytes("greaves" + '\n');
                                sendMessage("ammo");
                            }

                            else if (rs.getInt("is_shield_eq") == 1)
                            {
                                //dos.writeBytes("greaves" + '\n');
                                sendMessage("shield");
                            }

                            sendMessage("N:" + rs.getString("nome"));
                            //dos.writeBytes(rs.getString("nome") + '\n'); // Restituisco il nome dell'oggetto

                            sendMessage("D:" + rs.getString("descrizione"));
                            //dos.writeBytes(rs.getString("descrizione") + '\n'); // Restituisco la descrizione dell'oggetto

                            image = rs.getBlob("foto");
                            byte barr[] = image.getBytes(1,(int)image.length());
                            sleep(150);
                            dos.write(barr);

                            //dos.writeBytes( + '\n'); // Restituisco la foto dell'oggetto

                            sendMessage("V:" + rs.getString("valore"));
                            //dos.writeBytes(rs.getString("valore") + '\n'); // Restituisco il valore dell'oggetto




                        }


                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
                }

                else if (received.equals("get_equipment_from_category"))
                {
                    c_name = dis.readLine();

                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {

                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        number_items_b = 0;




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

                        else if (c_name.equals("magic"))
                        {
                            ob_type = "is_magic_eq";
                        }

                        else if (c_name.equals("other"))
                        {
                            ob_type = "is_other_eq";
                        }

                        else if (c_name.equals("arms"))
                        {
                            ob_type = "is_arms_eq";
                        }

                        else if (c_name.equals("legs"))
                        {
                            ob_type = "is_legs_eq";
                        }

                        else if (c_name.equals("ammo"))
                        {
                            ob_type = "is_ammo_eq";
                        }

                        else if (c_name.equals("shield"))
                        {
                            ob_type = "is_shield_eq";
                        }


                        else
                        {
                            ob_type = null;
                        }


                        rs = stmt.executeQuery("SELECT * from R_Personaggio_Oggetto rpo WHERE rpo.id_personaggio = " + character_id + " AND " + ob_type + " = 1");
                        while (rs.next()) {

                            number_items_b++;

                        }


                        sendMessage(Integer.toString(number_items_b));
                        //dos.writeBytes(Integer.toString(number_items_b) + '\n'); // Restituisco il numero di oggetti del backpack

                        image = null;

                        rs = stmt.executeQuery("SELECT * from R_Personaggio_Oggetto rpo WHERE rpo.id_personaggio = " + character_id + " AND " + ob_type + " = 1");
                        while (rs.next()) {



                            sendMessage("N:" + rs.getString("nome"));
                            //dos.writeBytes(rs.getString("nome") + '\n'); // Restituisco il nome dell'oggetto

                            sendMessage("D:" + rs.getString("descrizione"));
                            //dos.writeBytes(rs.getString("descrizione") + '\n'); // Restituisco la descrizione dell'oggetto

                            image = rs.getBlob("foto");
                            byte barr[] = image.getBytes(1,(int)image.length());
                            sleep(150);
                            dos.write(barr);

                            //dos.writeBytes( + '\n'); // Restituisco la foto dell'oggetto

                            sendMessage("V:" + rs.getString("valore"));
                            //dos.writeBytes(rs.getString("valore") + '\n'); // Restituisco il valore dell'oggetto

                        }


                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
                }

                //Aggiungere informazioni riguardante l'equipaggiamento
                //Restituire il numero di posti liberi per quel equipaggiamento madre
                else if (received.equals("get_avaible_number_equipment"))
                {
                    c_name = dis.readLine();

                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {

                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        if(c_name.equals("head"))
                        {
                            ob_type = "is_head_eq";
                            o_campo1 = "slot_head";
                        }
                        else if (c_name.equals("torso"))
                        {
                            ob_type = "is_torso_eq";
                            o_campo1 = "slot_torso";
                        }

                        else if (c_name.equals("left_arm"))
                        {
                            ob_type = "is_left_arm_eq";
                            o_campo1 = "slot_arms";
                        }

                        else if (c_name.equals("right_arm"))
                        {
                            ob_type = "is_right_arm_eq";
                            o_campo1 = "slot_arms";
                        }

                        else if (c_name.equals("left_leg"))
                        {
                            ob_type = "is_left_leg_eq";
                            o_campo1 = "slot_legs";
                        }

                        else if (c_name.equals("right_leg"))
                        {
                            ob_type = "is_right_leg_eq";
                            o_campo1 = "slot_legs";
                        }

                        else if (c_name.equals("first_weapon"))
                        {
                            ob_type = "is_first_weapon_eq";
                            o_campo1 = "slot_weapons";
                        }

                        else if (c_name.equals("secondary_weapon"))
                        {
                            ob_type = "is_secondary_weapon_eq";
                            o_campo1 = "slot_weapons";
                        }

                        else if (c_name.equals("gloves"))
                        {
                            ob_type = "is_gloves_eq";
                            o_campo1 = "slot_gloves";
                        }

                        else if (c_name.equals("left_gloves"))
                        {
                            ob_type = "is_left_gloves_eq";
                            o_campo1 = "slot_gloves";
                        }

                        else if (c_name.equals("right_gloves"))
                        {
                            ob_type = "is_right_gloves_eq";
                            o_campo1 = "slot_gloves";
                        }

                        else if (c_name.equals("shoes"))
                        {
                            ob_type = "is_shoes_eq";
                            o_campo1 = "slot_shoes";
                        }

                        else if (c_name.equals("greaves"))
                        {
                            ob_type = "is_greaves_eq";
                            o_campo1 = "slot_greaves";
                        }

                        else if (c_name.equals("left_greaves"))
                        {
                            ob_type = "is_left_greaves_eq";
                            o_campo1 = "slot_greaves";
                        }

                        else if (c_name.equals("right_greaves"))
                        {
                            ob_type = "is_right_greaves_eq";
                            o_campo1 = "slot_greaves";
                        }

                        else if (c_name.equals("magic"))
                        {
                            ob_type = "is_magic_eq";
                            o_campo1 = "slot_magic";
                        }

                        else if (c_name.equals("other"))
                        {
                            ob_type = "is_other_eq";
                            o_campo1 = "slot_other";
                        }

                        else if (c_name.equals("arms"))
                        {
                            ob_type = "is_arms_eq";
                            o_campo1 = "slot_arms";
                        }

                        else if (c_name.equals("legs"))
                        {
                            ob_type = "is_legs_eq";
                            o_campo1 = "slot_legs";
                        }

                        else if (c_name.equals("ammo"))
                        {
                            ob_type = "is_ammo_eq";
                            o_campo1 = "slot_ammo";
                        }

                        else if (c_name.equals("shield"))
                        {
                            ob_type = "is_shield_eq";
                            o_campo1 = "slot_shield";
                        }


                        else
                        {
                            ob_type = null;
                            o_campo1 = null;
                        }



                        slot = 0;

                        rs = stmt.executeQuery("SELECT * from Personaggi p WHERE p.id_personaggio = " + character_id + " AND p.id_sessione = " + session_id);
                        while (rs.next()) {

                            slot = rs.getInt(o_campo1);

                        }

                        slot2 = 0;

                        rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN Categorie c on o.id_categoria = c.id INNER JOIN R_Personaggio_Oggetto rpo on rpo.id_oggetto = o.id INNER JOIN Personaggi p on p.id = rpo.id_personaggio WHERE rpo.id_personaggio = " + character_id + " AND o.id_sessione = " + session_id + " AND rpo." + ob_type + " = '1'");
                        while (rs.next()) {

                            slot2 = slot2 + rs.getInt("numero_slot");

                        }


                        slot = slot - slot2;

                        sendMessage(Integer.toString(slot));

                    }
                    else
                        sendMessage("-1");
                }



                //SESSION

                //Creating session
                // RESTITUISCE 1 se ha già fatto il login oppure -1 se ha problemi. Poi restituisce il numero di sessioni per quell'utente e poi ad ogni sessione il numero di utenti online
                else if (received.equals("get_number_user_online_session"))
                {
                    if(is_disconnect == false && is_connect == true) {

                        sendMessage("1");

                        sendMessage(Integer.toString(number_sessions));
                        //dos.writeBytes(Integer.toString(number_sessions) + '\n');

                        if (number_sessions != 0) {
                            rs = stmt.executeQuery("SELECT * from Sessioni s inner join R_Utente_Sessione rus on rus.id_sessione = s.id where rus.id_utente = " + client_id);
                            while (rs.next()) {

                                //Calcolo ed invio degli utenti online per quella sessione
                                number_user_online_session = 0;
                                rs_temp = stmt.executeQuery("SELECT * from Utenti u inner join R_Utente_Sessione rus on rus.id_utente = u.id WHERE u.stato_login = 1 AND rus.id_sessione = " + rs.getString("id"));
                                while (rs_temp.next()) {
                                    number_user_online_session++;
                                }

                                sendMessage("NUO:" + Integer.toString(number_user_online_session));

                            }


                        }
                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
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
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');

                                if(rs.getInt("id_host") != client_id) //Se l'utente non è host allora mi fai vedere la vista dei characters
                                {
                                    sendMessage("1");
                                    is_host = false;
                                    rs_temp = stmt.executeQuery("SELECT * FROM Personaggi p WHERE p.id_sessione = " + session_id + " AND p.id_utente = " + client_id + " AND p.is_character = 1");
                                    while (rs_temp.next()) {
                                        number_characters++;
                                    }

                                    sendMessage("NC:" + Integer.toString(number_characters));
                                    //dos.writeBytes(Integer.toString(number_characters) + '\n'); //Restituisco il numero di characters per quell'utente

                                    rs_temp = stmt.executeQuery("SELECT * FROM Personaggi p WHERE p.id_sessione = " + session_id + " AND p.id_utente = " + client_id + " AND p.is_character = 1");
                                    while (rs_temp.next()) {

                                        sendMessage("N:" + rs_temp.getString("nome"));
                                        //dos.writeBytes(rs_temp.getString("nome") + '\n');

                                        sendMessage("HP:" + Integer.toString(rs_temp.getInt("hp")));
                                        //dos.writeBytes(Integer.toString(rs_temp.getInt("hp")) + '\n');

                                        sendMessage("HPMAX:" + Integer.toString(rs_temp.getInt("hp_max")));
                                        //dos.writeBytes(Integer.toString(rs_temp.getInt("hp_max")) + '\n');
                                    }


                                }

                                else
                                {
                                    sendMessage("2");
                                    //FUNZIONI PER IL GAME MASTER
                                    is_host = true;

                                }

                            }

                            i++;

                        }

                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
                }

                else if (received.equals("disconnect_session"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == false) {
                        character_id = 0;
                        session_id = 0;
                        is_host = false;
                        is_character_connected = false;
                        is_session_connected = false;
                        sendMessage("1");
                        //dos.writeBytes("1" + '\n');

                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
                }


                else if (received.equals("get_users_session"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true) {

                        if(number_sessions != 0)
                        {
                            sendMessage("1");
                            //dos.writeBytes("1" + '\n');

                            //Calcolo ed invio degli utenti online per quella sessione
                            number_user_online_session = 0;
                            rs_temp = stmt.executeQuery("SELECT * from Utenti u inner join R_Utente_Sessione rus on rus.id_utente = u.id WHERE rus.id_sessione = " + session_id);
                            while (rs_temp.next()) {
                                number_user_online_session++;
                            }
                            sendMessage(Integer.toString(number_user_online_session));
                            //dos.writeBytes(Integer.toString(number_user_online_session) + '\n');

                            rs_temp = stmt.executeQuery("SELECT * from Utenti u inner join R_Utente_Sessione rus on rus.id_utente = u.id WHERE rus.id_sessione = " + session_id);
                            while (rs_temp.next()) {

                                sendMessage("NOU:" + rs_temp.getString("nome_utente"));
                                //dos.writeBytes(rs_temp.getString("nome_utente") + '\n'); // Restituisco il nome dell'oggetto

                                if(rs_temp.getInt("stato_login") == 1)
                                {
                                    sendMessage("is_logged");
                                    //dos.writeBytes("is_logged" + '\n');
                                }
                                else if(rs_temp.getInt("stato_login") == 0)
                                {
                                    sendMessage("is_not_logged");
                                    //dos.writeBytes("is_not_logged" + '\n');
                                }

                                image = rs_temp.getBlob("imm_profilo");
                                byte barr[] = image.getBytes(1,(int)image.length());
                                sleep(150);
                                dos.write(barr);

                            }
                        }

                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
                }


                else if (received.equals("show_user_session"))
                {
                    if(is_disconnect == false && is_connect == true) {

                        if(number_sessions != 0)
                        {
                            sendMessage("1");
                            //dos.writeBytes("1" + '\n');


                            //Calcolo ed invio delle sessioni per quell'utente
                            number_sessions = 0;
                            rs_temp = stmt.executeQuery("SELECT * from Sessioni s inner join R_Utente_Sessione rus on rus.id_sessione = s.id where rus.id_utente = " + client_id);
                            while (rs_temp.next()) {
                                number_sessions++;
                            }
                            sendMessage(Integer.toString(number_sessions));  //Restituisco il numero di sessioni di quell'utente
                            //dos.writeBytes(Integer.toString(number_user_online_session) + '\n');



                            rs = stmt.executeQuery("SELECT * from Sessioni s inner join R_Utente_Sessione rus on rus.id_sessione = s.id where rus.id_utente = " + client_id);
                            while (rs.next()) {

                                //Invio del titolo
                                sendMessage("T:" + rs.getString("titolo"));
                                //dos.writeBytes(rs.getString("titolo") + '\n');

                                if (rs.getString("sottotitolo") == null) {
                                    sendMessage("S:0");
                                } else {
                                    System.out.println("SottoTitolo: " + rs.getString("sottotitolo"));
                                    sendMessage("S:" + rs.getString("sottotitolo"));
                                }

                                //Calcolo ed invio degli utenti online per quella sessione
                                number_user_online_session = 0;
                                rs_temp = stmt.executeQuery("SELECT * from Utenti u inner join R_Utente_Sessione rus on rus.id_utente = u.id WHERE u.stato_login = 1 AND rus.id_sessione = " + rs.getString("id"));
                                while (rs_temp.next()) {
                                    number_user_online_session++;
                                }
                                sendMessage("NUO:" + Integer.toString(number_user_online_session));
                                //dos.writeBytes(Integer.toString(number_user_online_session) + '\n');

                            }
                        }
                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
                }

                else if (received.equals("create_session"))
                {
                    s_titolo = dis.readLine();
                    s_sottotitolo = dis.readLine();

                    if(is_disconnect == false && is_connect == true)
                    {
                        sendMessage("1");
                        //dos.writeBytes("1" + '\n');



                        find_trade = false;
                        rs_temp = stmt.executeQuery("SELECT * FROM Sessioni WHERE Sessioni.titolo = '" + s_titolo + "'");
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

                            sendMessage("1");
                            //dos.writeBytes("1" + '\n');

                            sendMessage(Integer.toString(s_codice_invito));
                            //dos.writeBytes(Integer.toString(s_codice_invito) + '\n');

                        }

                        else
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        sendMessage("-1");
                        //dos.writeBytes("-1" + '\n');
                    }
                }

                //Add session
                else if (received.equals("add_session"))
                {
                    s_codice_invito = Integer.parseInt(dis.readLine());


                    if(is_disconnect == false && is_connect == true)
                    {
                        sendMessage("1");
                        //dos.writeBytes("1" + '\n');

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

                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');

                                //Inserimento nella tabella relazione con sessione e utente
                                pstmt = conn.prepareStatement("INSERT INTO R_Utente_Sessione " + "(id_utente, id_sessione) values (?,?)");

                                pstmt.setInt(1, client_id);
                                pstmt.setInt(2, send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse


                                //Invio del titolo
                                sendMessage("T:" + rs_temp.getString("titolo"));
                                //dos.writeBytes(rs_temp.getString("titolo") + '\n');

                                //Invio del sottotitolo
                                sendMessage("S:" + rs_temp.getString("sottotitolo"));
                                //dos.writeBytes(rs_temp.getString("sottotitolo") + '\n');


                                //Calcolo ed invio degli utenti online per quella sessione
                                number_user_online_session = 0;
                                rs = stmt.executeQuery("SELECT * from Utenti u inner join R_Utente_Sessione rus on rus.id_utente = u.id WHERE u.stato_login = 1 AND rus.id_sessione = " + send_id);
                                while (rs.next()) {
                                    number_user_online_session++;
                                }

                                sendMessage("NUO:" + Integer.toString(number_user_online_session));
                                //dos.writeBytes(Integer.toString(number_user_online_session) + '\n');




                            }


                        }

                        else
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }



                    }
                    else
                    {
                        sendMessage("-1");
                        //dos.writeBytes("-1" + '\n');
                    }
                }

                //Show all items of that session
                else if (received.equals("show_items_session"))
                {
                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true) {

                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        number_items_b = 0;

                        rs = stmt.executeQuery("SELECT * from Oggetti o WHERE o.id_sessione = " + session_id);
                        while (rs.next()) {
                            number_items_b++;
                        }

                        sendMessage(Integer.toString(number_items_b));
                        //dos.writeBytes(Integer.toString(number_items_b) + '\n'); // Restituisco il numero di oggetti del backpack

                        image = null;

                        rs = stmt.executeQuery("SELECT * from Oggetti o WHERE rpo.id_sessione = " + session_id);
                        while (rs.next()) {

                            sendMessage("N:" + rs.getString("nome"));
                            //dos.writeBytes(rs.getString("nome") + '\n'); // Restituisco il nome dell'oggetto

                            sendMessage("D:" + rs.getString("descrizione"));
                            //dos.writeBytes(rs.getString("descrizione") + '\n'); // Restituisco la descrizione dell'oggetto

                            image = rs.getBlob("foto");
                            byte barr[] = image.getBytes(1,(int)image.length());
                            sleep(150);
                            dos.write(barr);

                            //dos.writeBytes( + '\n'); // Restituisco la foto dell'oggetto

                            sendMessage("V:" + rs.getString("valore"));
                            //dos.writeBytes(rs.getString("valore") + '\n'); // Restituisco il valore dell'oggetto

                            sendMessage("Q:" + rs.getString("quantita"));
                            //dos.writeBytes(rs.getString("quantita") + '\n'); // Restituisco la quantita dell'oggetto


                            sendMessage("RC:" + rs.getString("rarita_colore")); //Mando la rarita del colore

                            sendMessage("PO:" + Float.toString(rs.getFloat("potenza"))); //Mando la potenza

                            sendMessage("PE:" + Float.toString(rs.getFloat("peso"))); //Mando il peso

                            sendMessage("DUR:" + Integer.toString(rs.getInt("durabilita"))); //Mando la durabilita
                        }


                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
                }

                else if (received.equals("update_session_data"))
                {
                    o_nome = dis.readLine();
                    ob_type = dis.readLine();
                    ob_data = dis.readLine();


                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true)
                    {
                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;

                        rs = stmt.executeQuery("SELECT * from Sessioni p WHERE p.titolo = '" + o_nome + "' AND p.id_host = " + client_id);
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
                                pstmt = conn.prepareStatement("UPDATE Sessioni SET titolo = '" + ob_data + "' where Sessioni.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("sottotitolo"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Sessioni SET sottotitolo = '" + ob_data + "' where Sessioni.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("codice_invito"))
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


                                pstmt = conn.prepareStatement("UPDATE Sessioni SET codice_invito = '" + s_codice_invito + "' where Sessioni.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage(Integer.toString(s_codice_invito));
                                //dos.writeBytes("1" + '\n');
                            }


                            //UPDATE FOTO

                            else
                            {
                                sendMessage("-3");
                                //dos.writeBytes("-3" + '\n');
                            }

                        }
                        else
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        sendMessage("-1");
                        //dos.writeBytes("-1" + '\n');
                    }
                }

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
                        sendMessage(Integer.toString(number_users_online));
                        //dos.writeBytes(Integer.toString(number_users_online) + '\n');


                        // creo la tabella
                        if(number_users_online > 0) {
                            rs = stmt.executeQuery("SELECT * from Utenti where Utenti.stato_login = '1'");

                            toreturn = "";

                            while (rs.next()) {
                                toreturn = rs.getString("nome_utente");
                                //toreturn = " NAME: " + rs.getString("nome") + "  SURNAME: " + rs.getString("cognome") + "  USERNAME: " + rs.getString("nome_utente");
                                System.out.println(toreturn);
                                sendMessage("NU:" + toreturn);
                                //dos.writeBytes(toreturn + '\n');

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



                // Game master



                //Update punti skill // DA non fare adesso

                //Show all monsters

                //Show all npcs

                //Show all characters

                //Show all transport



                //Category

                else if (received.equals("get_category_data"))
                {
                    o_nome = dis.readLine();
                    ob_type = dis.readLine();


                    if(is_disconnect == false && is_connect == true && is_session_connected == true)
                    {
                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;


                        rs = stmt.executeQuery("SELECT * from Categorie p WHERE p.id_sessione = " + session_id + " AND p.nome = '" + o_nome + "'");
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
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(rs.getString("descrizione"));
                                    //dos.writeBytes(rs.getString("descrizione") + '\n');
                                }

                                else if(ob_type.equals("tipo_potenza"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(rs.getString("descrizione"));
                                    //dos.writeBytes(rs.getString("tipo_potenza") + '\n');
                                }


                                //GET FOTO


                                else if(ob_type.equals("head"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Integer.toString(rs.getInt("is_head_eq")));
                                    //dos.writeBytes(Integer.toString(rs.getInt("is_head_eq")) + '\n');
                                }

                                else if(ob_type.equals("torso"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Integer.toString(rs.getInt("is_torso_eq")));
                                    //dos.writeBytes(Integer.toString(rs.getInt("is_torso_eq")) + '\n');
                                }

                                else if(ob_type.equals("left_arm"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Integer.toString(rs.getInt("is_left_arm_eq")));
                                    //dos.writeBytes(Integer.toString(rs.getInt("is_left_arm_eq")) + '\n');
                                }

                                else if(ob_type.equals("right_arm"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Integer.toString(rs.getInt("is_right_arm_eq")));
                                    //dos.writeBytes(Integer.toString(rs.getInt("is_right_arm_eq")) + '\n');
                                }

                                else if(ob_type.equals("left_leg"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Integer.toString(rs.getInt("is_left_leg_eq")));
                                    //dos.writeBytes(Integer.toString(rs.getInt("is_left_leg_eq")) + '\n');
                                }

                                else if(ob_type.equals("right_leg"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Integer.toString(rs.getInt("is_right_leg_eq")));
                                    //dos.writeBytes(Integer.toString(rs.getInt("is_right_leg_eq")) + '\n');
                                }

                                else if(ob_type.equals("first_weapon"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Integer.toString(rs.getInt("is_first_weapon_eq")));
                                    //dos.writeBytes(Integer.toString(rs.getInt("is_first_weapon_eq")) + '\n');
                                }

                                else if(ob_type.equals("secondary_weapon"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Integer.toString(rs.getInt("is_secondary_weapon_eq")));
                                    //dos.writeBytes(Integer.toString(rs.getInt("is_secondary_weapon_eq")) + '\n');
                                }

                                else if(ob_type.equals("gloves"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Integer.toString(rs.getInt("is_gloves_eq")));
                                    //dos.writeBytes(Integer.toString(rs.getInt("is_gloves_eq")) + '\n');
                                }

                                else if(ob_type.equals("left_gloves"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Integer.toString(rs.getInt("is_left_gloves_eq")));
                                    //dos.writeBytes(Integer.toString(rs.getInt("is_left_gloves_eq")) + '\n');
                                }

                                else if(ob_type.equals("right_gloves"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Integer.toString(rs.getInt("is_right_gloves_eq")));
                                    //dos.writeBytes(Integer.toString(rs.getInt("is_right_gloves_eq")) + '\n');
                                }

                                else if(ob_type.equals("shoes"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Integer.toString(rs.getInt("is_shoes_eq")));
                                    //dos.writeBytes(Integer.toString(rs.getInt("is_shoes_eq")) + '\n');
                                }

                                else if(ob_type.equals("greaves"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Integer.toString(rs.getInt("is_greaves_eq")));
                                    //dos.writeBytes(Integer.toString(rs.getInt("is_greaves_eq")) + '\n');
                                }

                                else if(ob_type.equals("left_greaves"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Integer.toString(rs.getInt("is_left_greaves_eq")));
                                    //dos.writeBytes(Integer.toString(rs.getInt("is_left_greaves_eq")) + '\n');
                                }

                                else if(ob_type.equals("right_greaves"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Integer.toString(rs.getInt("is_right_greaves_eq")));
                                    //dos.writeBytes(Integer.toString(rs.getInt("is_right_greaves_eq")) + '\n');
                                }

                                else if(ob_type.equals("magic"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Integer.toString(rs.getInt("is_magic_eq")));
                                    //dos.writeBytes(Integer.toString(rs.getInt("is_magic_eq")) + '\n');
                                }

                                else if(ob_type.equals("other"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Integer.toString(rs.getInt("is_other_eq")));
                                    //dos.writeBytes(Integer.toString(rs.getInt("is_other_eq")) + '\n');
                                }

                                else if(ob_type.equals("arms"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Integer.toString(rs.getInt("is_arms_eq")));
                                    //dos.writeBytes(Integer.toString(rs.getInt("is_arms_eq")) + '\n');
                                }

                                else if(ob_type.equals("legs"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Integer.toString(rs.getInt("is_legs_eq")));
                                    //dos.writeBytes(Integer.toString(rs.getInt("is_legs_eq")) + '\n');
                                }

                                else if(ob_type.equals("ammo"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Integer.toString(rs.getInt("is_ammo_eq")));
                                    //dos.writeBytes(Integer.toString(rs.getInt("is_ammo_eq")) + '\n');
                                }

                                else if(ob_type.equals("shield"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Integer.toString(rs.getInt("is_shield_eq")));
                                    //dos.writeBytes(Integer.toString(rs.getInt("is_ammo_eq")) + '\n');
                                }

                                else if(ob_type.equals("numero_slot"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Integer.toString(rs.getInt("numero_slot")));
                                    //dos.writeBytes(Integer.toString(rs.getInt("numero_slot")) + '\n');
                                }

                                else
                                {
                                    sendMessage("-3");
                                    //dos.writeBytes("-3" + '\n');
                                }
                            }



                        }
                        else
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        sendMessage("-1");
                        //dos.writeBytes("-1" + '\n');
                    }
                }


                else if (received.equals("update_category_data"))
                {
                    o_nome = dis.readLine();
                    ob_type = dis.readLine();
                    ob_data = dis.readLine();

                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true)
                    {
                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;




                        rs = stmt.executeQuery("SELECT * from Categorie p WHERE p.id_sessione = " + session_id + " AND p.nome = '" + o_nome + "'");
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
                                pstmt = conn.prepareStatement("UPDATE Categorie SET nome = '" + ob_data + "' where Categorie.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("descrizione"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET descrizione = '" + ob_data + "' where Categorie.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("tipo_potenza"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET tipo_potenza = '" + ob_data + "' where Categorie.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("head"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET is_head_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("torso"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET is_torso_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("left_arm"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET is_left_arm_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("right_arm"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET is_right_arm_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("left_leg"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET is_left_leg_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("right_leg"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET is_right_leg_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("first_weapon"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET is_first_weapon_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("secondary_weapon"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET is_secondary_weapon_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("gloves"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET is_gloves_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("left_gloves"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET is_left_gloves_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("right_gloves"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET is_right_gloves_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("shoes"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET is_shoes_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("greaves"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET is_greaves_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("left_greaves"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET is_left_greaves_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("right_greaves"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET is_right_greaves_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("magic"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET is_magic_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("other"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET is_other_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("arms"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET is_arms_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("legs"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET is_legs_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("ammo"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET is_ammo_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("shield"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET is_shield_eq = '1' WHERE Categorie.id = '" + send_id + "'");
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("numero_slot"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Categorie SET numero_slot = '" + Integer.parseInt(ob_data) + "' where Categorie.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }



                            //UPDATE FOTO

                            else
                            {
                                sendMessage("-3");
                                //dos.writeBytes("-3" + '\n');
                            }

                        }
                        else
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        sendMessage("-1");
                        //dos.writeBytes("-1" + '\n');
                    }
                }


                else if (received.equals("get_avaible_category_items"))
                {
                    ob_type = dis.readLine(); //Mi prendo il nome della category


                    if(is_disconnect == false && is_connect == true && is_session_connected == true) {
                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma
                        find_object = false;


                        if(ob_type.equals("head"))
                        {
                            find_object = true;
                            ob_type = "is_head_eq";

                        }

                        else if(ob_type.equals("torso"))
                        {
                            find_object = true;
                            ob_type = "is_torso_eq";
                        }

                        else if(ob_type.equals("left_arm"))
                        {
                            find_object = true;
                            ob_type = "is_left_arm_eq";
                        }

                        else if(ob_type.equals("right_arm"))
                        {
                            find_object = true;
                            ob_type = "is_right_arm_eq";
                        }

                        else if(ob_type.equals("left_leg"))
                        {
                            find_object = true;
                            ob_type = "is_left_leg_eq";
                        }

                        else if(ob_type.equals("right_leg"))
                        {
                            find_object = true;
                            ob_type = "is_right_leg_eq";
                        }

                        else if(ob_type.equals("first_weapon"))
                        {
                            find_object = true;
                            ob_type = "is_first_weapon_eq";
                        }

                        else if(ob_type.equals("secondary_weapon"))
                        {
                            find_object = true;
                            ob_type = "is_secondary_weapon_eq";
                        }

                        else if(ob_type.equals("gloves"))
                        {
                            find_object = true;
                            ob_type = "is_gloves_eq";
                        }

                        else if(ob_type.equals("left_gloves"))
                        {
                            find_object = true;
                            ob_type = "is_left_gloves_eq";
                        }

                        else if(ob_type.equals("right_gloves"))
                        {
                            find_object = true;
                            ob_type = "is_right_gloves_eq";
                        }

                        else if(ob_type.equals("shoes"))
                        {
                            find_object = true;
                            ob_type = "is_shoes_eq";
                        }

                        else if(ob_type.equals("greaves"))
                        {
                            find_object = true;
                            ob_type = "is_greaves_eq";
                        }

                        else if(ob_type.equals("left_greaves"))
                        {
                            find_object = true;
                            ob_type = "is_left_greaves_eq";
                        }

                        else if(ob_type.equals("right_greaves"))
                        {
                            find_object = true;
                            ob_type = "is_right_greaves_eq";
                        }

                        else if(ob_type.equals("magic"))
                        {
                            find_object = true;
                            ob_type = "is_magic_eq";
                        }

                        else if(ob_type.equals("other"))
                        {
                            find_object = true;
                            ob_type = "is_other_eq";
                        }

                        else if(ob_type.equals("arms"))
                        {
                            find_object = true;
                            ob_type = "is_arms_eq";
                        }

                        else if(ob_type.equals("legs"))
                        {
                            find_object = true;
                            ob_type = "is_legs_eq";
                        }

                        else if(ob_type.equals("ammo"))
                        {
                            find_object = true;
                            ob_type = "is_ammo_eq";
                        }

                        else if(ob_type.equals("shield"))
                        {
                            find_object = true;
                            ob_type = "is_shield_eq";
                        }

                        if(find_object) {

                            number_object_categories = 0;


                            rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN Categorie c on c.id = o.id_categoria INNER JOIN R_Personaggio_Oggetto rpo on rpo.id_oggetto = o.id WHERE o.id_sessione = '" + session_id + "' AND rpo.id_personaggio = '" + character_id + "' AND c." + ob_type + " = '1' AND (rpo.is_head_eq = 0 AND rpo.is_torso_eq = 0 AND rpo.is_left_arm_eq = 0 AND rpo.is_right_arm_eq = 0 AND rpo.is_left_leg_eq = 0 AND rpo.is_right_leg_eq = 0 AND rpo.is_first_weapon_eq = 0 AND rpo.is_secondary_weapon_eq = 0 AND rpo.is_gloves_eq = 0 AND rpo.is_left_gloves_eq = 0 AND rpo.is_right_gloves_eq = 0 AND rpo.is_shoes_eq = 0 AND rpo.is_greaves_eq = 0 AND rpo.is_left_greaves_eq = 0 AND rpo.is_right_greaves_eq = 0 AND rpo.is_arms_eq = 0 AND rpo.is_legs_eq = 0 AND rpo.is_ammo_eq = 0 AND rpo.is_other_eq = 0 AND rpo.is_magic_eq = 0 AND rpo.is_shield_eq = 0)");
                            while (rs.next()) {
                                number_object_categories++;
                            }

                            sendMessage(Integer.toString(number_object_categories));

                            rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN Categorie c on c.id = o.id_categoria INNER JOIN R_Personaggio_Oggetto rpo on rpo.id_oggetto = o.id WHERE o.id_sessione = '" + session_id + "' AND rpo.id_personaggio = '" + character_id + "' AND c." + ob_type + " = '1' AND (rpo.is_head_eq = 0 AND rpo.is_torso_eq = 0 AND rpo.is_left_arm_eq = 0 AND rpo.is_right_arm_eq = 0 AND rpo.is_left_leg_eq = 0 AND rpo.is_right_leg_eq = 0 AND rpo.is_first_weapon_eq = 0 AND rpo.is_secondary_weapon_eq = 0 AND rpo.is_gloves_eq = 0 AND rpo.is_left_gloves_eq = 0 AND rpo.is_right_gloves_eq = 0 AND rpo.is_shoes_eq = 0 AND rpo.is_greaves_eq = 0 AND rpo.is_left_greaves_eq = 0 AND rpo.is_right_greaves_eq = 0 AND rpo.is_arms_eq = 0 AND rpo.is_legs_eq = 0 AND rpo.is_ammo_eq = 0 AND rpo.is_other_eq = 0 AND rpo.is_magic_eq = 0 AND rpo.is_shield_eq = 0)");
                            while (rs.next()) {

                                sendMessage("N:" + rs.getString("o.nome")); //Restituisco il nome

                                sendMessage("R:" + rs.getString("rarita_colore")); //Restituisco la rarita

                                sendMessage("PE:" + Float.toString(rs.getFloat("peso")));

                                sendMessage("DU:" + Integer.toString(rs.getInt("durabilita")));

                                sendMessage("PO:" + Float.toString(rs.getFloat("potenza")));

                                sendMessage("QU:" + Integer.toString(rs.getInt("quantita")));

                                sendMessage("NC:" + rs.getString("c.nome")); //Restituisco il nome della sottocategoria
                            }

                        }
                        else
                        {
                            sendMessage("-2");
                        }

                    }
                    else
                        sendMessage("-1");
                }



                //Object

                else if (received.equals("get_object_data"))
                {
                    o_nome = dis.readLine();
                    ob_type = dis.readLine();

                    if(is_disconnect == false && is_connect == true && is_session_connected == true)
                    {
                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;


                        rs = stmt.executeQuery("SELECT * from Oggetti o WHERE o.id_sessione = " + session_id + " AND o.nome = '" + o_nome + "'");
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
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(rs.getString("descrizione"));
                                    //dos.writeBytes(rs.getString("descrizione") + '\n');
                                }

                                else if(ob_type.equals("valore"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Float.toString(rs.getFloat("valore")));
                                    //dos.writeBytes( Float.toString(rs.getFloat("valore")) + '\n');
                                }

                                else if(ob_type.equals("campo1"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(rs.getString("campo1"));
                                    //dos.writeBytes(rs.getString("campo1") + '\n');
                                }

                                else if(ob_type.equals("campo2"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(rs.getString("campo2"));
                                    //dos.writeBytes(rs.getString("campo2") + '\n');
                                }

                                else if(ob_type.equals("campo3"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(rs.getString("campo3"));
                                    //dos.writeBytes(rs.getString("campo3") + '\n');
                                }

                                else if(ob_type.equals("campo4"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(rs.getString("campo4"));
                                    //dos.writeBytes(rs.getString("campo4") + '\n');
                                }

                                else if(ob_type.equals("campo5"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(rs.getString("campo5"));
                                    //dos.writeBytes(rs.getString("campo5") + '\n');
                                }

                                else if(ob_type.equals("rarita_colore"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(rs.getString("rarita_colore"));
                                    //dos.writeBytes(rs.getString("rarita_colore") + '\n');
                                }

                                else if(ob_type.equals("peso"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Float.toString(rs.getFloat("peso")));
                                    //dos.writeBytes( Float.toString(rs.getFloat("peso")) + '\n');
                                }

                                else if(ob_type.equals("durabilita"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Integer.toString(rs.getInt("durabilita")));
                                    //dos.writeBytes( Integer.toString(rs.getInt("durabilita")) + '\n');
                                }

                                else if(ob_type.equals("potenza"))
                                {
                                    sendMessage("1");
                                    //dos.writeBytes("1" + '\n');

                                    sendMessage(Float.toString(rs.getFloat("potenza")));
                                    //dos.writeBytes( Float.toString(rs.getFloat("potenza")) + '\n');
                                }

                                //GET FOTO

                                else
                                {
                                    sendMessage("-3");
                                    //dos.writeBytes("-3" + '\n');
                                }
                            }



                        }
                        else
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        sendMessage("-1");
                        //dos.writeBytes("-1" + '\n');
                    }
                }

                else if (received.equals("update_object_data"))
                {
                    o_nome = dis.readLine();
                    ob_type = dis.readLine();
                    ob_data = dis.readLine();

                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true)
                    {
                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;

                        rs = stmt.executeQuery("SELECT * from Oggetti o WHERE o.id_sessione = " + session_id + " AND o.nome = '" + o_nome + "'");
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
                                pstmt = conn.prepareStatement("UPDATE Oggetti SET nome = '" + ob_data + "' where Oggetti.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("descrizione"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Oggetti SET descrizione = '" + ob_data + "' where Oggetti.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("valore"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Oggetti SET valore = '" + Float.parseFloat(ob_data) + "' where Oggetti.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("campo1"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Oggetti SET campo1 = '" + ob_data + "' where Oggetti.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("campo2"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Oggetti SET campo2 = '" + ob_data + "' where Oggetti.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("campo3"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Oggetti SET campo3 = '" + ob_data + "' where Oggetti.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("campo4"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Oggetti SET campo4 = '" + ob_data + "' where Oggetti.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("campo5"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Oggetti SET campo5 = '" + ob_data + "' where Oggetti.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("rarita"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Oggetti SET rarita_colore = '" + ob_data + "' where Oggetti.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("peso"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Oggetti SET peso = '" + Float.parseFloat(ob_data) + "' where Oggetti.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("durabilita"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Oggetti SET durabilita = '" + Integer.parseInt(ob_data) + "' where Oggetti.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else if(ob_type.equals("potenza"))
                            {
                                pstmt = conn.prepareStatement("UPDATE Oggetti SET potenza = '" + Float.parseFloat(ob_data) + "' where Oggetti.id = " + send_id);
                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse
                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            //UPDATE FOTO


                            else
                            {
                                sendMessage("-3");
                                //dos.writeBytes("-3" + '\n');
                            }

                        }
                        else
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        sendMessage("-1");
                        //dos.writeBytes("-1" + '\n');
                    }
                }

                //Adding object to equipment con le restrizioni appropiate per gli slot e lo spazio a disposizione
                else if (received.equals("add_object_equipment"))
                {
                    c_name = dis.readLine();
                    what_character = dis.readLine();

                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true)
                    {
                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;


                        rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN R_Personaggio_Oggetto rpo on o.id = rpo.id_oggetto INNER JOIN Personaggio p on p.id = rpo.id_personaggio WHERE p.id_sessione = " + session_id + " AND p.id = " + character_id + " AND o.nome = '" + c_name + "'");
                        while (rs.next())
                        {
                            if(c_name.equals(rs.getString("o.nome"))) {
                                find_category = true;
                                send_id = rs.getInt("o.id");
                            }
                        }


                        if(find_category == true)
                        {
                            can_be = false;

                            ob_type = what_character;


                            if(c_name.equals("head"))
                            {
                                ob_type = "is_head_eq";
                                o_campo1 = "slot_head";
                            }
                            else if (c_name.equals("torso"))
                            {
                                ob_type = "is_torso_eq";
                                o_campo1 = "slot_torso";
                            }

                            else if (c_name.equals("left_arm"))
                            {
                                ob_type = "is_left_arm_eq";
                                o_campo1 = "slot_arms";
                            }

                            else if (c_name.equals("right_arm"))
                            {
                                ob_type = "is_right_arm_eq";
                                o_campo1 = "slot_arms";
                            }

                            else if (c_name.equals("left_leg"))
                            {
                                ob_type = "is_left_leg_eq";
                                o_campo1 = "slot_legs";
                            }

                            else if (c_name.equals("right_leg"))
                            {
                                ob_type = "is_right_leg_eq";
                                o_campo1 = "slot_legs";
                            }

                            else if (c_name.equals("first_weapon"))
                            {
                                ob_type = "is_first_weapon_eq";
                                o_campo1 = "slot_weapons";
                            }

                            else if (c_name.equals("secondary_weapon"))
                            {
                                ob_type = "is_secondary_weapon_eq";
                                o_campo1 = "slot_weapons";
                            }

                            else if (c_name.equals("gloves"))
                            {
                                ob_type = "is_gloves_eq";
                                o_campo1 = "slot_gloves";
                            }

                            else if (c_name.equals("left_gloves"))
                            {
                                ob_type = "is_left_gloves_eq";
                                o_campo1 = "slot_gloves";
                            }

                            else if (c_name.equals("right_gloves"))
                            {
                                ob_type = "is_right_gloves_eq";
                                o_campo1 = "slot_gloves";
                            }

                            else if (c_name.equals("shoes"))
                            {
                                ob_type = "is_shoes_eq";
                                o_campo1 = "slot_shoes";
                            }

                            else if (c_name.equals("greaves"))
                            {
                                ob_type = "is_greaves_eq";
                                o_campo1 = "slot_greaves";
                            }

                            else if (c_name.equals("left_greaves"))
                            {
                                ob_type = "is_left_greaves_eq";
                                o_campo1 = "slot_greaves";
                            }

                            else if (c_name.equals("right_greaves"))
                            {
                                ob_type = "is_right_greaves_eq";
                                o_campo1 = "slot_greaves";
                            }

                            else if (c_name.equals("magic"))
                            {
                                ob_type = "is_magic_eq";
                                o_campo1 = "slot_magic";
                            }

                            else if (c_name.equals("other"))
                            {
                                ob_type = "is_other_eq";
                                o_campo1 = "slot_other";
                            }

                            else if (c_name.equals("arms"))
                            {
                                ob_type = "is_arms_eq";
                                o_campo1 = "slot_arms";
                            }

                            else if (c_name.equals("legs"))
                            {
                                ob_type = "is_legs_eq";
                                o_campo1 = "slot_legs";
                            }

                            else if (c_name.equals("ammo"))
                            {
                                ob_type = "is_ammo_eq";
                                o_campo1 = "slot_ammo";
                            }

                            else if (c_name.equals("shield"))
                            {
                                ob_type = "is_shield_eq";
                                o_campo1 = "slot_shield";
                            }


                            else
                            {
                                ob_type = null;
                                o_campo1 = null;
                            }

                            slot = 0;

                            rs = stmt.executeQuery("SELECT * from Personaggi p WHERE p.id_personaggio = " + character_id + " AND p.id_sessione = " + session_id);
                            while (rs.next()) {

                                slot = rs.getInt(o_campo1);

                            }

                            slot2 = 0;

                            rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN Categorie c on o.id_categoria = c.id INNER JOIN R_Personaggio_Oggetto rpo on rpo.id_oggetto = o.id INNER JOIN Personaggi p on p.id = rpo.id_personaggio WHERE rpo.id_personaggio = " + character_id + " AND o.id_sessione = " + session_id + " AND rpo." + ob_type + " = '1'");
                            while (rs.next()) {

                                slot2 = slot2 + rs.getInt("numero_slot");

                            }



                            rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN Categorie c on o.id_categoria = c.id WHERE o.id = " + send_id);
                            while (rs.next()) {

                                slot2 = slot2 + rs.getInt("numero_slot");

                            }

                            slot = slot - slot2;

                            if(slot < 0)
                                can_be = false;
                            else
                                can_be = true;



                            if(can_be) {


                                pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_head_eq = '0', is_torso_eq = '0', is_left_arm_eq = '0', is_right_arm_eq = '0', is_left_leg_eq = '0', is_right_leg_eq = '0', is_first_weapon_eq = '0', is_secondary_weapon_eq = '0', is_gloves_eq = '0', is_left_gloves_eq = '0', is_right_gloves_eq = '0', is_shoes_eq = '0', is_greaves_eq = '0', is_left_greaves_eq = '0', is_right_greaves_eq = '0', is_arms_eq = '0', is_other_eq = '0', is_magic_eq = '0', is_shield_eq = '0' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                                pstmt.execute();
                                pstmt.close();


                                if (what_character.equals("head")) {
                                    pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_head_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                                } else if (what_character.equals("torso")) {
                                    pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_torso_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                                } else if (what_character.equals("left_arm")) {
                                    pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_left_arm_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                                } else if (what_character.equals("right_arm")) {
                                    pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_right_arm_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                                } else if (what_character.equals("left_leg")) {
                                    pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_left_leg_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                                } else if (what_character.equals("right_leg")) {
                                    pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_right_leg_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                                } else if (what_character.equals("first_weapon")) {
                                    pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_first_weapon_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                                } else if (what_character.equals("secondary_weapon")) {
                                    pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_secondary_weapon_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                                } else if (what_character.equals("gloves")) {
                                    pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_gloves_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                                } else if (what_character.equals("left_gloves")) {
                                    pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_left_gloves_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                                } else if (what_character.equals("right_gloves")) {
                                    pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_right_gloves_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                                } else if (what_character.equals("shoes")) {
                                    pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_shoes_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                                } else if (what_character.equals("greaves")) {
                                    pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_greaves_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                                } else if (what_character.equals("left_greaves")) {
                                    pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_left_greaves_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                                } else if (what_character.equals("right_greaves")) {
                                    pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_right_greaves_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                                } else if (what_character.equals("arms")) {
                                    pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_arms_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                                } else if (what_character.equals("legs")) {
                                    pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_legs_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                                } else if (what_character.equals("ammo")) {
                                    pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_ammo_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                                } else if (what_character.equals("shield")) {
                                    pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto SET is_shield_eq = '1' WHERE R_Personaggio_Oggetto.id_oggetto = " + send_id + " AND R_Personaggio_Oggetto.id_personaggio = " + character_id);
                                }

                                pstmt.execute();
                                pstmt.close(); // rilascio le risorse

                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');

                            }
                            else
                                sendMessage("-3"); // Restituisce il messaggio che l'aggiunta di questo nuovo equipaggiamento non può essere fatta perchè ha raggiunto il limite massimo di oggetti da poter equipaggiare
                        }
                        else
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }

                    }
                    else
                    {
                        sendMessage("-1");
                        //dos.writeBytes("-1" + '\n');
                    }
                }

                //Remove object from equipment
                else if (received.equals("remove_object_equipment"))
                {
                    o_nome = dis.readLine(); //Mi prendo il nome dell'oggetto
                    ob_quantity = Integer.parseInt(dis.readLine());


                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {


                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_object = false;


                        rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN R_Personaggio_Oggetto rpo on rpo.id_oggetto = o.id WHERE rpo.id_personaggio = " + character_id + " AND o.id_sessione = " + session_id + " AND o.nome = '" + o_nome + "'");
                        while (rs.next()) {

                            find_object = true;
                            ob_id = rs.getInt("o.id");
                        }


                        if(find_object == false)
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }

                        else
                        {
                            pstmt = conn.prepareStatement("UPDATE R_Personaggio_Oggetto rpo SET rpo.is_head_eq = 0, rpo.is_torso_eq = 0, rpo.is_left_arm_eq = 0, rpo.is_right_arm_eq = 0, rpo.is_left_leg_eq = 0, rpo.is_right_leg_eq = 0, rpo.is_first_weapon_eq = 0, rpo.is_secondary_weapon_eq = 0, rpo.is_gloves_eq = 0, rpo.is_left_gloves_eq = 0, rpo.is_right_gloves_eq = 0, rpo.is_shoes_eq = 0, rpo.is_greaves_eq = 0, rpo.is_left_greaves_eq = 0, rpo.is_right_greaves_eq = 0, rpo.is_arms_eq = 0, rpo.is_legs_eq = 0, rpo.is_ammo_eq = 0, rpo.is_other_eq = 0, rpo.is_magic_eq = 0, rpo.is_shield_eq = 0 WHERE rpo.id_oggetto = " + ob_id + " AND rpo.id_personaggio = " + character_id);
                            pstmt.execute();
                            pstmt.close(); // rilascio le risorse

                            sendMessage("1");

                        }

                    }
                    else
                        sendMessage("-1");
                }

                //ADD object to category
                else if (received.equals("add_object_category"))
                {
                    c_name = dis.readLine(); //Mi prendo il nome della category
                    o_nome = dis.readLine(); //Mi prendo il nome dell'oggetto
                    ob_quantity = Integer.parseInt(dis.readLine());


                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true) {


                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;
                        find_object = false;


                        rs = stmt.executeQuery("SELECT * from Categorie c WHERE c.id_sessione = " + session_id + " AND c.nome = '" + c_name + "'");
                        while (rs.next()) {
                            find_character = true;
                            ch_id = rs.getInt("id");
                        }

                        rs = stmt.executeQuery("SELECT * from Oggetti o WHERE o.id_sessione = " + session_id + " AND o.nome = '" + o_nome + "'");
                        while (rs.next()) {

                            find_object = true;
                            ob_id = rs.getInt("id");
                        }

                        if(find_category == false)
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }

                        else if(find_object == false)
                        {
                            sendMessage("-3");
                            //dos.writeBytes("-3" + '\n');
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

                                sendMessage("1");
                                //dos.writeBytes("1" + '\n');
                            }

                            else
                            {
                                sendMessage("-4");
                                //dos.writeBytes("-4" + '\n');
                            }

                        }

                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
                }


                //ADD object to character
                else if (received.equals("add_object_character"))
                {
                    c_name = dis.readLine(); //Mi prendo il nome del character
                    o_nome = dis.readLine(); //Mi prendo il nome dell'oggetto
                    ob_quantity = Integer.parseInt(dis.readLine());


                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true) {


                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_character = false;
                        find_object = false;

                        rs = stmt.executeQuery("SELECT * from Personaggi p WHERE p.id_sessione = " + session_id + " AND p.nome = '" + c_name + "'");
                        while (rs.next()) {

                            find_character = true;
                            ch_id = rs.getInt("id");
                        }

                        rs = stmt.executeQuery("SELECT * from Oggetti o WHERE o.id_sessione = " + session_id + " AND o.nome = '" + o_nome + "'");
                        while (rs.next()) {

                            find_object = true;
                            ob_id = rs.getInt("id");
                        }

                        if(find_character == false)
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }

                        else if(find_object == false)
                        {
                            sendMessage("-3");
                            //dos.writeBytes("-3" + '\n');
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

                            sendMessage("1");
                            //dos.writeBytes("1" + '\n');
                        }

                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
                }

                //Remove object from character
                else if (received.equals("remove_object_character"))
                {
                    o_nome = dis.readLine(); //Mi prendo il nome dell'oggetto
                    ob_quantity = Integer.parseInt(dis.readLine());


                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_character_connected == true) {


                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_object = false;


                        rs = stmt.executeQuery("SELECT * from Oggetti o INNER JOIN R_Personaggio_Oggetto rpo on rpo.id_oggetto = o.id WHERE rpo.id_personaggio = " + character_id + " AND o.id_sessione = " + session_id + " AND o.nome = '" + o_nome + "'");
                        while (rs.next()) {

                            find_object = true;
                            ob_id = rs.getInt("o.id");
                        }


                        if(find_object == false)
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }

                        else
                        {
                            pstmt = conn.prepareStatement("DELETE FROM R_Personaggio_Oggetto WHERE id_personaggio = ? AND id_oggetto = ?");
                            pstmt.setInt(1, character_id);
                            pstmt.setInt(2, ob_id);
                            pstmt.execute();
                            pstmt.close(); // rilascio le risorse
                            sendMessage("1");
                            //dos.writeBytes("1" + '\n');
                        }

                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
                }

                //Creating object
                else if (received.equals("create_object"))
                {
                    o_nome = dis.readLine(); //Mi prendo il nome dell'oggetto
                    o_descrizione = dis.readLine(); //Mi prendo la descrizione dell'oggetto
                    o_rarita = dis.readLine();
                    o_campo1 = dis.readLine();
                    o_campo2 = dis.readLine();
                    o_campo3 = dis.readLine();
                    o_campo4 = dis.readLine();
                    o_campo5 = dis.readLine();
                    o_valore = Float.parseFloat(dis.readLine());
                    o_peso = Float.parseFloat(dis.readLine());
                    o_potenza = Float.parseFloat(dis.readLine());
                    o_durabilita = Integer.parseInt(dis.readLine());



                    if(is_disconnect == false && is_connect == true && is_session_connected == true && is_host == true) {


                        sendMessage("1");
                        //dos.writeBytes("1" + '\n'); // Restituisco la conferma

                        find_category = false;



                        //FOTO


                        rs = stmt.executeQuery("SELECT * from Oggetti c WHERE o.id_sessione = " + session_id);
                        while (rs.next()) {

                            if(o_nome.equals(rs.getString("nome")))
                                find_category = true;

                        }

                        if(find_category)
                        {
                            sendMessage("-2");
                            //dos.writeBytes("-2" + '\n');
                        }

                        else
                        {
                            pstmt = conn.prepareStatement("INSERT INTO Oggetti " + "(nome, descrizione, valore, campo1, campo2, campo3, campo4, campo5, rarita_colore, id_sessione, peso, durabilita, potenza) values (?,?,?,?,?,?,?,?,?,?,?,?,?)");


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

                            pstmt.setFloat(11, o_peso);
                            pstmt.setInt(12, o_durabilita);
                            pstmt.setFloat(13, o_potenza);

                            pstmt.execute();
                            pstmt.close(); // rilascio le risorse

                            sendMessage("1");
                            //dos.writeBytes("1" + '\n');
                        }

                    }
                    else
                        sendMessage("-1");
                    //dos.writeBytes("-1" + '\n');
                }




                //SERVER
                else if(received.equals("close_connection")) 
                {  
                	//CHIUSURA DELLA CONNESSIONE
                    is_closed = true;
                    System.out.println("Client " + this.s + " want to close the connection...");
                    System.out.println("Closing this connection.");
                    sendMessage("end");

                    System.out.println("Connection closed");

                    sleep(1000);

                    pstmt.close(); // rilascio le risorse
                    stmt.close(); // rilascio le risorse
                    conn.close(); // termino la connessione
                    conn_temp.close();
                    stmt_temp.close();

                    this.dis.close();
                    this.dos.close();
                    this.s.close();

                    break; 
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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } 
          
        try
        { 
            // closing resources
            System.out.println("\nClosing Resources\n");
            this.dis.close(); 
            this.dos.close();
            this.writer.close();
            if(pstmt != null)
                pstmt.close();

            if(stmt != null)
                stmt.close();

            if(stmt_temp != null)
                stmt_temp.close();

            if(conn_temp != null)
                conn_temp.close();

            if(conn != null)
                conn.close();
              
        }catch(IOException | SQLException e){
            e.printStackTrace(); 
        } 
    } 
}