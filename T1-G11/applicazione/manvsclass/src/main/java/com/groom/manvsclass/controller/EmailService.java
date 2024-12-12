package com.groom.manvsclass.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private final JavaMailSender javaMailSender;

  @Autowired
  public EmailService(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  // MAIL RESET PASSWORD
  public void sendPasswordResetEmail(String email, String resetToken) throws MessagingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    helper.setTo(email);
    helper.setSubject("Richiesta di reset password");
    helper.setText("Per favore, copia il seguente token per resettare la password: " + resetToken);

    javaMailSender.send(message);
  }
    // MAIL DI INVITO
  public void sendInvitationToken(String email, String invitationToken) throws MessagingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    helper.setTo(email);
    helper.setSubject("Invio token di invito");
    helper.setText("Per favore, copia il seguente token presso l'end-point \\login_with_invitation per registrarti come amministratore: " + invitationToken);

    javaMailSender.send(message);
  }

    //Modifica 12/12/2024
    //Mail aggiunta Team // MAIL DI NOTIFICA AGGIUNTA AL TEAM
    public void sendTeamAdditionNotificationToStudents(List<String> emails, String teamName) throws MessagingException {
      for (String email : emails) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Sei stato aggiunto a un nuovo Team!");
        helper.setText("Ciao,\n\n" +
                      "Sei stato aggiunto al team: \"" + teamName+"\n" +
                      "Accedi alla piattaforma per maggiori dettagli.\n\n");

        javaMailSender.send(message);
      }
    }
    //Mail nuovo assignment agli studenti.

    public void sendTeamNewAssignment(List<String> emails, String teamName, java.util.Date dataScadenza, String titoloAssignment, String descrizione ) throws MessagingException {
      for (String email : emails) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Aggiungi destinatario
        helper.setTo(email);

        // Oggetto dell'email
        helper.setSubject("Nuovo assignment per il team: " + teamName);

        // Contenuto dell'email
        String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(dataScadenza);
        String body = "Ciao,\n\n" +
                      "Hai un nuovo assignment nel team: \"" + teamName + "\".\n\n" +
                      "Titolo Assignment: " + titoloAssignment + "\n" +
                      "Descrizione: " + descrizione + "\n\n" +
                      "Data di scadenza: " + formattedDate + "\n\n" +
                      "Accedi alla piattaforma per maggiori dettagli e per completarlo.";

        helper.setText(body);

        // Invia l'email
        javaMailSender.send(message);

    } 
  }
}