#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/ip.h>
#include <netinet/in.h>
#include <unistd.h>
#include <pthread.h>
#include <libpq-fe.h>

// Costanti
#define PORT_NUMBER 8080
#define BUFFER_SIZE 2000

// Struttura dati per gli utenti
struct User
{
    char username[50];
    char password[50];
    char accessibility[100];
    int flag;
};

// Prototipi delle funzioni
PGconn *establishDBConnection(PGconn *connection);
bool registerUser(struct User user, PGconn *connection);
bool loginUser(struct User *user, PGconn *connection);
void *handleConnection(void *socketDesc);
void sendMessageToClient(char *message, int socket);

int main(void)
{
    int serverSocket, clientSocket;
    struct sockaddr_in serverAddress, clientAddress;
    socklen_t clientAddressLength = sizeof(struct sockaddr_in);

    // Creazione del socket del server
    serverSocket = socket(AF_INET, SOCK_STREAM, 0);
    if (serverSocket == -1)
    {
        perror("Error creating socket");
        exit(EXIT_FAILURE);
    }

    // Configurazione dell'indirizzo del server
    serverAddress.sin_family = AF_INET;
    serverAddress.sin_addr.s_addr = INADDR_ANY;
    serverAddress.sin_port = htons(PORT_NUMBER);

    // Associazione del socket all'indirizzo del server
    if (bind(serverSocket, (struct sockaddr *)&serverAddress, sizeof(serverAddress)) < 0)
    {
        perror("Bind failed");
        exit(EXIT_FAILURE);
    }

    // Ascolto delle connessioni in arrivo
    listen(serverSocket, 3);

    printf("Server MovieHub online\n");
    printf("Server in ascolto su porta %d...\n\n", PORT_NUMBER);

    // Accettazione delle connessioni e gestione dei thread
    while ((clientSocket = accept(serverSocket, (struct sockaddr *)&clientAddress, &clientAddressLength)))
    {
        printf("Connection with client established\n");

        pthread_t thread;
        int *newSocket = malloc(sizeof(int));
        if (!newSocket)
        {
            perror("Error allocating memory");
            exit(EXIT_FAILURE);
        }
        *newSocket = clientSocket;

        if (pthread_create(&thread, NULL, handleConnection, (void *)newSocket) != 0)
        {
            perror("Error creating thread");
            free(newSocket);
            exit(EXIT_FAILURE);
        }
    }
    if (clientSocket < 0)
    {
        perror("Connection with client failed\n");
        exit(EXIT_FAILURE);
    }

    return 0;
}

// Connessione al database
PGconn *establishDBConnection(PGconn *connection)
{
    char *connectionInfo = "host=192.168.56.1 port=5432 dbname=moviehub user=postgres password=admin";
    connection = PQconnectdb(connectionInfo);

    if (PQstatus(connection) != CONNECTION_OK)
    {
        fprintf(stderr, "Database connection failed: %s\n", PQerrorMessage(connection));
        PQfinish(connection);
        exit(EXIT_FAILURE);
    }
    else
    {
        printf("Database connection established successfully\n\n");
    }

    return connection;
}

// Registrazione di un nuovo utente
bool registerUser(struct User user, PGconn *connection)
{
    char query[1000];
    sprintf(query, "INSERT INTO users(username, password, accessibility) VALUES ('%s', '%s', '%s')", user.username, user.password, user.accessibility);

    PGresult *result = PQexec(connection, query);
    if (PQresultStatus(result) != PGRES_COMMAND_OK)
    {
        fprintf(stderr, "Error executing query: %s", PQerrorMessage(connection));
        PQclear(result);
        exit(EXIT_FAILURE);
    }
    PQclear(result);

    return true;
}

// Login utente
bool loginUser(struct User *user, PGconn *connection)
{
    char querySelect[1000];
    if (user->flag > 0)
    {
        sprintf(querySelect, "SELECT accessibility FROM users WHERE username='%s'", user->username);
    }
    else
    {
        sprintf(querySelect, "SELECT accessibility FROM users WHERE username='%s' AND password='%s'", user->username, user->password);
    }

    PGresult *result = PQexec(connection, querySelect);
    if (PQresultStatus(result) != PGRES_TUPLES_OK)
    {
        fprintf(stderr, "Error executing query: %s", PQerrorMessage(connection));
        PQclear(result);
        exit(EXIT_FAILURE);
    }

    int rows = PQntuples(result);
    if (rows > 0)
    {
        // Se l'utente è stato trovato, aggiorna la sua accessibilità
        strcpy(user->accessibility, PQgetvalue(result, 0, 0));
        PQclear(result);
        return true;
    }
    else
    {
        PQclear(result);
        return false;
    }
}

// Funzione per gestire la connessione di un client
void *handleConnection(void *socketDesc)
{
    int clientSocket = *(int *)socketDesc;
    struct User user;
    char message[255], clientMessage[BUFFER_SIZE];
    PGconn *connection = establishDBConnection(connection);

    // Ricevi un messaggio dal client
    while (recv(clientSocket, clientMessage, sizeof(clientMessage), 0) > 0)
    {
        char *token = strtok(clientMessage, ";");

        if (token != NULL)
        {
            if (strcmp(token, "Registrazione") == 0)
            {
                printf("Register new user...\n");

                token = strtok(NULL, ";");
                strcpy(user.username, token);

                token = strtok(NULL, ";");
                strcpy(user.password, token);

                token = strtok(NULL, ";");
                strcpy(user.accessibility, token);

                if (registerUser(user, connection))
                {
                    printf("Registration was successful\n\n");
                    strcpy(message, "OK");
                }
                else
                {
                    printf("Error during registration\n\n");
                    strcpy(message, "Errore");
                }
            }

            if (strcmp(token, "Login") == 0)
            {
                printf("Login utente ...\n\n");

                token = strtok(NULL, ";");
                strcpy(user.username, token);

                token = strtok(NULL, ";");
                strcpy(user.password, token);

                if (loginUser(&user, connection))
                {
                    printf("Login successful\n\n");
                    strcpy(message, user.accessibility);
                }
                else
                {
                    printf("Error during login\n\n");
                    strcpy(message, "Errore");
                }
            }

            sendMessageToClient(message, clientSocket);
        }
    }

    // Gestione della disconnessione del client
    ssize_t recvResult = recv(clientSocket, message, sizeof(message), 0);
    if (recvResult == 0)
    {
        puts("Client disconnected\n\n");
        fflush(stdout);
        close(clientSocket);
    }
    else if (recvResult == (ssize_t)-1)
    {
        perror("recv failed");
        close(clientSocket);
    }

    // Chiusura del socket e liberazione delle risorse
    close(clientSocket);
    PQfinish(connection);
    free(socketDesc);
    return 0;
}

// Funzione per inviare un messaggio al client
void sendMessageToClient(char *message, int sock)
{
    if (write(sock, message, strlen(message)) < 0)
    {
        perror("Error writing to socket\n\n");
        exit(EXIT_FAILURE);
    }
}
