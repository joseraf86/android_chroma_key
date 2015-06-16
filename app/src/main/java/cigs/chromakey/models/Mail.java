package cigs.chromakey.models;

import android.net.Uri;

/**
 *
 */
public class Mail {

    private String[] to;      // indicates primary recipients (multiple allowed)
    private String subject; // a brief summary of the topic of the message.
                            // Certain abbreviations are commonly used in the subject, including "RE:" and "FW:".
    //private String bcc;     // Blind carbon copy;
                            // addresses added to the SMTP delivery list but not (usually) listed in the message data,
                            // remaining invisible to other recipients.
    //private String cc;      // Carbon copy;
                            // Many email clients will mark email in one's inbox differently depending on whether
                            // they are in the To: or Cc: list.
    //private String contentType; // Information about how the message is to be displayed, usually a MIME type.
    //private String precedence;  // commonly with values "bulk", "junk", or "list";
    // used to indicate that automated "vacation" or "out of office" responses should not be returned for this mail,
    // e.g. to prevent vacation notices from being sent to all other subscribers of a mailing list.
    // Sendmail uses this header to affect prioritization of queued email, with "Precedence: special-delivery" messages delivered sooner.
    // With modern high-bandwidth networks delivery priority is less of an issue than it once was.
    // Microsoft Exchange respects a fine-grained automatic response suppression mechanism, the X-Auto-Response-Suppress header.[67]
    //private String references;  // Message-ID of the message that this is a reply to, and the message-id
    // of the message the previous reply was a reply to, etc.
    //private String replyTo;     // Address that should be used to reply to the message.
    //private String sender;      // Address of the actual sender acting on behalf of the author listed in the From: field (secretary, list manager, etc.).
    //private String archivedAt;  // A direct link to the archived form of an individual email message.

    private String body;
    private Uri attachment;


    // Getters ans Setters

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public void attach( Uri attachment )
    {
        this.attachment = attachment;
    }

    public Uri getAttachment(){
        return attachment;
    }







}
