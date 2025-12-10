package com.fastcar.util;

import com.fastcar.model.Agent;
import com.fastcar.model.Client;
import com.fastcar.model.Contrat;
import com.fastcar.model.Voiture;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Generates HTML invoices that can be printed to PDF from browser
 */
public class PDFInvoiceGenerator {

    /**
     * Generates an HTML invoice that can be printed to PDF
     */
    public static File generateHTMLInvoice(Contrat contrat, Client client, Voiture voiture, Agent agent) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "invoices/Facture_" + timestamp + ".html";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Ensure directory exists
        new File("invoices").mkdirs();

        StringBuilder html = new StringBuilder();

        // HTML Header with Styling
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"fr\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>Facture - ").append(contrat.getId()).append("</title>\n");
        html.append("    <style>\n");
        html.append("        body {\n");
        html.append("            font-family: 'Arial', sans-serif;\n");
        html.append("            margin: 0;\n");
        html.append("            padding: 20px;\n");
        html.append("            background-color: #f5f5f5;\n");
        html.append("        }\n");
        html.append("        .container {\n");
        html.append("            max-width: 800px;\n");
        html.append("            margin: 0 auto;\n");
        html.append("            background-color: white;\n");
        html.append("            padding: 40px;\n");
        html.append("            border: 1px solid #ddd;\n");
        html.append("            box-shadow: 0 2px 4px rgba(0,0,0,0.1);\n");
        html.append("        }\n");
        html.append("        .header {\n");
        html.append("            text-align: center;\n");
        html.append("            border-bottom: 3px solid #2c3e50;\n");
        html.append("            padding-bottom: 20px;\n");
        html.append("            margin-bottom: 30px;\n");
        html.append("        }\n");
        html.append("        .header h1 {\n");
        html.append("            margin: 0;\n");
        html.append("            color: #2c3e50;\n");
        html.append("            font-size: 28px;\n");
        html.append("        }\n");
        html.append("        .header p {\n");
        html.append("            margin: 5px 0;\n");
        html.append("            color: #666;\n");
        html.append("            font-size: 14px;\n");
        html.append("        }\n");
        html.append("        .section {\n");
        html.append("            margin-bottom: 25px;\n");
        html.append("            page-break-inside: avoid;\n");
        html.append("        }\n");
        html.append("        .section-title {\n");
        html.append("            background-color: #34495e;\n");
        html.append("            color: white;\n");
        html.append("            padding: 10px 15px;\n");
        html.append("            margin-bottom: 15px;\n");
        html.append("            font-size: 16px;\n");
        html.append("            font-weight: bold;\n");
        html.append("        }\n");
        html.append("        .info-table {\n");
        html.append("            width: 100%;\n");
        html.append("            border-collapse: collapse;\n");
        html.append("        }\n");
        html.append("        .info-table td {\n");
        html.append("            padding: 8px 15px;\n");
        html.append("            border-bottom: 1px solid #ecf0f1;\n");
        html.append("        }\n");
        html.append("        .info-table .label {\n");
        html.append("            font-weight: bold;\n");
        html.append("            width: 35%;\n");
        html.append("            color: #2c3e50;\n");
        html.append("        }\n");
        html.append("        .info-table .value {\n");
        html.append("            color: #555;\n");
        html.append("        }\n");
        html.append("        .total-section {\n");
        html.append("            background-color: #ecf0f1;\n");
        html.append("            padding: 15px;\n");
        html.append("            margin: 20px 0;\n");
        html.append("            border-left: 4px solid #27ae60;\n");
        html.append("        }\n");
        html.append("        .total-amount {\n");
        html.append("            font-size: 24px;\n");
        html.append("            font-weight: bold;\n");
        html.append("            color: #27ae60;\n");
        html.append("            text-align: right;\n");
        html.append("        }\n");
        html.append("        .footer {\n");
        html.append("            margin-top: 40px;\n");
        html.append("            padding-top: 20px;\n");
        html.append("            border-top: 2px solid #ddd;\n");
        html.append("            text-align: center;\n");
        html.append("            font-size: 12px;\n");
        html.append("            color: #777;\n");
        html.append("        }\n");
        html.append("        .footer-info {\n");
        html.append("            line-height: 1.6;\n");
        html.append("        }\n");
        html.append("        .print-button {\n");
        html.append("            display: block;\n");
        html.append("            width: 150px;\n");
        html.append("            margin: 20px auto;\n");
        html.append("            padding: 10px 20px;\n");
        html.append("            background-color: #3498db;\n");
        html.append("            color: white;\n");
        html.append("            border: none;\n");
        html.append("            border-radius: 4px;\n");
        html.append("            font-size: 14px;\n");
        html.append("            cursor: pointer;\n");
        html.append("        }\n");
        html.append("        .print-button:hover {\n");
        html.append("            background-color: #2980b9;\n");
        html.append("        }\n");
        html.append("        @media print {\n");
        html.append("            body {\n");
        html.append("                background-color: white;\n");
        html.append("            }\n");
        html.append("            .print-button {\n");
        html.append("                display: none;\n");
        html.append("            }\n");
        html.append("        }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <div class=\"container\">\n");

        // Header
        html.append("        <div class=\"header\">\n");
        html.append("            <h1>FACTURE DE LOCATION</h1>\n");
        html.append("            <p>AGENCE FASTCAR MAROC</p>\n");
        html.append("        </div>\n");

        // Contract Info
        html.append("        <div class=\"section\">\n");
        html.append("            <div class=\"section-title\">INFORMATIONS CONTRAT</div>\n");
        html.append("            <table class=\"info-table\">\n");
        html.append("                <tr>\n");
        html.append("                    <td class=\"label\">N° Contrat</td>\n");
        html.append("                    <td class=\"value\">").append(contrat.getId()).append("</td>\n");
        html.append("                </tr>\n");
        html.append("                <tr>\n");
        html.append("                    <td class=\"label\">Date début</td>\n");
        html.append("                    <td class=\"value\">").append(sdf.format(contrat.getDateDebut())).append("</td>\n");
        html.append("                </tr>\n");
        html.append("                <tr>\n");
        html.append("                    <td class=\"label\">Date fin</td>\n");
        html.append("                    <td class=\"value\">").append(sdf.format(contrat.getDateFin())).append("</td>\n");
        html.append("                </tr>\n");
        html.append("                <tr>\n");
        html.append("                    <td class=\"label\">Mode de paiement</td>\n");
        html.append("                    <td class=\"value\">").append(contrat.getModePaiement()).append("</td>\n");
        html.append("                </tr>\n");
        html.append("            </table>\n");
        html.append("        </div>\n");

        // Client Info
        html.append("        <div class=\"section\">\n");
        html.append("            <div class=\"section-title\">INFORMATIONS CLIENT</div>\n");
        html.append("            <table class=\"info-table\">\n");
        html.append("                <tr>\n");
        html.append("                    <td class=\"label\">CIN</td>\n");
        html.append("                    <td class=\"value\">").append(client.getCin()).append("</td>\n");
        html.append("                </tr>\n");
        html.append("                <tr>\n");
        html.append("                    <td class=\"label\">Nom</td>\n");
        html.append("                    <td class=\"value\">").append(client.getNom()).append("</td>\n");
        html.append("                </tr>\n");
        html.append("                <tr>\n");
        html.append("                    <td class=\"label\">Prénom</td>\n");
        html.append("                    <td class=\"value\">").append(client.getPrenom()).append("</td>\n");
        html.append("                </tr>\n");
        html.append("                <tr>\n");
        html.append("                    <td class=\"label\">Adresse</td>\n");
        html.append("                    <td class=\"value\">").append(client.getAdresse()).append("</td>\n");
        html.append("                </tr>\n");
        html.append("                <tr>\n");
        html.append("                    <td class=\"label\">Téléphone</td>\n");
        html.append("                    <td class=\"value\">").append(client.getTelephone()).append("</td>\n");
        html.append("                </tr>\n");
        html.append("                <tr>\n");
        html.append("                    <td class=\"label\">Email</td>\n");
        html.append("                    <td class=\"value\">").append(client.getEmail()).append("</td>\n");
        html.append("                </tr>\n");
        html.append("            </table>\n");
        html.append("        </div>\n");

        // Vehicle Info
        html.append("        <div class=\"section\">\n");
        html.append("            <div class=\"section-title\">INFORMATIONS VÉHICULE</div>\n");
        html.append("            <table class=\"info-table\">\n");
        html.append("                <tr>\n");
        html.append("                    <td class=\"label\">Matricule</td>\n");
        html.append("                    <td class=\"value\">").append(voiture.getMatricule()).append("</td>\n");
        html.append("                </tr>\n");
        html.append("                <tr>\n");
        html.append("                    <td class=\"label\">Marque</td>\n");
        html.append("                    <td class=\"value\">").append(voiture.getMarque()).append("</td>\n");
        html.append("                </tr>\n");
        html.append("                <tr>\n");
        html.append("                    <td class=\"label\">Modèle</td>\n");
        html.append("                    <td class=\"value\">").append(voiture.getModele()).append("</td>\n");
        html.append("                </tr>\n");
        html.append("                <tr>\n");
        html.append("                    <td class=\"label\">Prix journalier</td>\n");
        html.append("                    <td class=\"value\">").append(String.format("%.2f MAD", voiture.getPrixJournalier())).append("</td>\n");
        html.append("                </tr>\n");
        html.append("                <tr>\n");
        html.append("                    <td class=\"label\">État</td>\n");
        html.append("                    <td class=\"value\">").append(voiture.getEtat()).append("</td>\n");
        html.append("                </tr>\n");
        html.append("            </table>\n");
        html.append("        </div>\n");

        // Agent Info
        html.append("        <div class=\"section\">\n");
        html.append("            <div class=\"section-title\">AGENT</div>\n");
        html.append("            <table class=\"info-table\">\n");
        if (agent != null) {
            html.append("                <tr>\n");
            html.append("                    <td class=\"label\">N° Agent</td>\n");
            html.append("                    <td class=\"value\">").append(agent.getNumAgent()).append("</td>\n");
            html.append("                </tr>\n");
            html.append("                <tr>\n");
            html.append("                    <td class=\"label\">Nom</td>\n");
            html.append("                    <td class=\"value\">").append(agent.getNom()).append("</td>\n");
            html.append("                </tr>\n");
            html.append("                <tr>\n");
            html.append("                    <td class=\"label\">Prénom</td>\n");
            html.append("                    <td class=\"value\">").append(agent.getPrenom()).append("</td>\n");
            html.append("                </tr>\n");
        } else {
            html.append("                <tr>\n");
            html.append("                    <td class=\"label\">Agent</td>\n");
            html.append("                    <td class=\"value\">Non spécifié</td>\n");
            html.append("                </tr>\n");
        }
        html.append("            </table>\n");
        html.append("        </div>\n");

        // Total Amount
        html.append("        <div class=\"total-section\">\n");
        html.append("            <div style=\"font-size: 16px; margin-bottom: 10px;\">Montant Total</div>\n");
        html.append("            <div class=\"total-amount\">").append(String.format("%.2f MAD", contrat.getMontant())).append("</div>\n");
        html.append("        </div>\n");

        // Print Button
        html.append("        <button class=\"print-button\" onclick=\"window.print()\">🖨️ Imprimer / Enregistrer en PDF</button>\n");

        // Footer
        html.append("        <div class=\"footer\">\n");
        html.append("            <div class=\"footer-info\">\n");
        html.append("                <p><strong>FASTCAR MAROC</strong></p>\n");
        html.append("                <p>Siège social : Bd Mohammed V, Casablanca</p>\n");
        html.append("                <p>Tél : 05 22 33 44 55 | RC : 123456</p>\n");
        html.append("                <p>Patente : 78901234 | IF : 98765432</p>\n");
        html.append("                <p style=\"margin-top: 15px; font-style: italic;\">Merci de votre confiance !</p>\n");
        html.append("            </div>\n");
        html.append("        </div>\n");

        html.append("    </div>\n");
        html.append("</body>\n");
        html.append("</html>\n");

        File file = new File(fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(html.toString());
            System.out.println("HTML Invoice generated: " + file.getAbsolutePath());
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
