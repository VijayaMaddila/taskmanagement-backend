package com.taskmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendProjectInvite(String toEmail, String projectName, String inviteToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("You're invited to join: " + projectName);
            helper.setText(buildInviteHtml(toEmail, projectName, inviteToken), true);

            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send invite email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send invite email: " + e.getMessage());
        }
    }

    private String buildInviteHtml(String toEmail, String projectName, String token) {
        String registerLink = frontendUrl + "/register?token=" + token + "&email=" + toEmail;

        return "<!DOCTYPE html>"
            + "<html lang='en'><head><meta charset='UTF-8'>"
            + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
            + "<title>Project Invite</title></head>"
            + "<body style='margin:0;padding:0;background-color:#f4f6f9;font-family:Arial,Helvetica,sans-serif'>"

            // wrapper
            + "<table width='100%' cellpadding='0' cellspacing='0' style='background-color:#f4f6f9;padding:40px 0'>"
            + "<tr><td align='center'>"

            // card
            + "<table width='600' cellpadding='0' cellspacing='0' style='"
            + "background-color:#ffffff;border-radius:12px;"
            + "box-shadow:0 4px 24px rgba(0,0,0,0.08);overflow:hidden'>"

            // header
            + "<tr><td style='"
            + "background:linear-gradient(135deg,#4F46E5 0%,#7C3AED 100%);"
            + "padding:40px 48px;text-align:center'>"
            + "<h1 style='margin:0;color:#ffffff;font-size:28px;font-weight:700;letter-spacing:-0.5px'>"
            + "TaskManagement</h1>"
            + "<p style='margin:8px 0 0;color:rgba(255,255,255,0.8);font-size:14px'>Project Collaboration Platform</p>"
            + "</td></tr>"

            // body
            + "<tr><td style='padding:48px'>"

            // icon
            + "<div style='text-align:center;margin-bottom:32px'>"
            + "<div style='display:inline-block;background:#EEF2FF;border-radius:50%;width:72px;height:72px;line-height:72px;text-align:center'>"
            + "<span style='font-size:32px'>📋</span>"
            + "</div></div>"

            // heading
            + "<h2 style='margin:0 0 12px;color:#1e1b4b;font-size:22px;font-weight:700;text-align:center'>"
            + "You've been invited!</h2>"
            + "<p style='margin:0 0 32px;color:#6b7280;font-size:15px;line-height:1.6;text-align:center'>"
            + "You have been invited to collaborate on the project<br>"
            + "<strong style='color:#4F46E5;font-size:17px'>" + projectName + "</strong></p>"

            // info box
            + "<div style='background:#f8fafc;border:1px solid #e2e8f0;border-radius:8px;padding:20px 24px;margin-bottom:32px'>"
            + "<table width='100%' cellpadding='0' cellspacing='0'>"
            + "<tr>"
            + "<td style='color:#64748b;font-size:13px;padding:6px 0'>📧 Invited email</td>"
            + "<td style='color:#1e293b;font-size:13px;font-weight:600;text-align:right;padding:6px 0'>" + toEmail + "</td>"
            + "</tr>"
            + "<tr>"
            + "<td style='color:#64748b;font-size:13px;padding:6px 0'>📁 Project</td>"
            + "<td style='color:#1e293b;font-size:13px;font-weight:600;text-align:right;padding:6px 0'>" + projectName + "</td>"
            + "</tr>"
            + "</table></div>"

            // CTA button
            + "<div style='text-align:center;margin-bottom:32px'>"
            + "<a href='" + registerLink + "' style='"
            + "display:inline-block;padding:16px 40px;"
            + "background:linear-gradient(135deg,#4F46E5 0%,#7C3AED 100%);"
            + "color:#ffffff;text-decoration:none;border-radius:8px;"
            + "font-size:16px;font-weight:700;letter-spacing:0.3px;"
            + "box-shadow:0 4px 14px rgba(79,70,229,0.4)'>"
            + "Accept Invite &amp; Register →</a></div>"

            // note
            + "<div style='background:#fffbeb;border:1px solid #fde68a;border-radius:8px;padding:16px 20px;margin-bottom:24px'>"
            + "<p style='margin:0;color:#92400e;font-size:13px;line-height:1.5'>"
            + "⚠️ <strong>Note:</strong> Your email address is pre-filled and cannot be changed during registration. "
            + "This ensures the invite is linked to the correct account.</p>"
            + "</div>"

            + "<p style='margin:0;color:#9ca3af;font-size:13px;text-align:center'>"
            + "Already have an account? <a href='" + frontendUrl + "/login' style='color:#4F46E5;text-decoration:none;font-weight:600'>Log in here</a></p>"

            + "</td></tr>"

            // footer
            + "<tr><td style='background:#f8fafc;border-top:1px solid #e5e7eb;padding:24px 48px;text-align:center'>"
            + "<p style='margin:0 0 8px;color:#9ca3af;font-size:12px'>"
            + "This invite was sent by TaskManagement. If you didn't expect this, you can safely ignore it.</p>"
            + "<p style='margin:0;color:#d1d5db;font-size:11px'>© 2026 TaskManagement. All rights reserved.</p>"
            + "</td></tr>"

            + "</table>"
            + "</td></tr></table>"
            + "</body></html>";
    }
}
