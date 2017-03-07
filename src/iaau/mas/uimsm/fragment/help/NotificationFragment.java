package iaau.mas.uimsm.fragment.help;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import iaau.mas.uimsm.HomeActivity;
import iaau.mas.uimsm.LoginActivity;
import iaau.mas.uimsm.R;

/**
 * Created by Administrator on 12.02.2014.
 */
public class NotificationFragment extends SherlockFragment
{
    private View _fragmentView;
    private String server = LoginActivity.local_address;
    private String url_page_en = server + "//UIMS/Help/notification/en.html";
    private String url_page_ru = server + "//UIMS/Help/notification/ru.html";
    private String url_page_kg = server + "//UIMS/Help/notification/kg.html";
    private String url_page_tr = server + "//UIMS/Help/notification/tr.html";
    ProgressDialog mProgressDialog;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

//      Without setHasOptionsMenu(true),
//      a fragment wonâ€™t receive the callbacks
//      when Action Bar items are selected.
        setHasOptionsMenu(true);

        new ContentEng().execute();
    }

    private class ContentEng extends AsyncTask<Void, Void, Void>
    {
        @SuppressWarnings("unused")
		String title;
        String head;
        String body;
        String foot;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getSherlockActivity());
            mProgressDialog.setTitle(R.string.progress_lang_en);
            mProgressDialog.setMessage("Notification web content is loading..");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try{
                // Connecting to the web resource
                Document document = Jsoup.connect(url_page_en).get();
                title = document.title();
                //Element header = document.getElementById("head");
                Elements header = document.select("div[id=head]");
                Elements body_text = document.select("div[id=body-text]");
                Elements footer = document.select("div[id=footer]");
                head = header.toString();
                body = body_text.toString();
                foot = footer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            TextView header_text = (TextView) getSherlockActivity().findViewById(R.id.notification_head);
            TextView body_text = (TextView) getSherlockActivity().findViewById(R.id.notification_body);
            TextView footer_text = (TextView) getSherlockActivity().findViewById(R.id.notification_foot);

            header_text.setText(Html.fromHtml(head));
            body_text.setText(Html.fromHtml(body));
            footer_text.setText(Html.fromHtml(foot));

            mProgressDialog.dismiss();
        }
    }


    private class ContentRU extends AsyncTask<Void, Void, Void>
    {
        @SuppressWarnings("unused")
		String title;
        String head;
        String body;
        String foot;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getSherlockActivity());
            mProgressDialog.setTitle(R.string.progress_lang_ru);
            mProgressDialog.setMessage("Уведомление веб-контент загружается");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try{
                // Connecting to the web resource
                Document document = Jsoup.connect(url_page_ru).get();
                title = document.title();
                //Element header = document.getElementById("head");
                Elements header = document.select("div[id=head]");
                Elements body_text = document.select("div[id=body-text]");
                Elements footer = document.select("div[id=footer]");
                head = header.toString();
                body = body_text.toString();
                foot = footer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            TextView header_text = (TextView) getSherlockActivity().findViewById(R.id.notification_head);
            TextView body_text = (TextView) getSherlockActivity().findViewById(R.id.notification_body);
            TextView footer_text = (TextView) getSherlockActivity().findViewById(R.id.notification_foot);

            header_text.setText(Html.fromHtml(head));
            body_text.setText(Html.fromHtml(body));
            footer_text.setText(Html.fromHtml(foot));

            mProgressDialog.dismiss();
        }
    }


    private class ContentKG extends AsyncTask<Void, Void, Void>
    {
        @SuppressWarnings("unused")
		String title;
        String head;
        String body;
        String foot;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getSherlockActivity());
            mProgressDialog.setTitle(R.string.progress_lang_kg);
            mProgressDialog.setMessage("Уведомление веб-контент загружается");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try{
                // Connecting to the web resource
                Document document = Jsoup.connect(url_page_kg).get();
                title = document.title();
                //Element header = document.getElementById("head");
                Elements header = document.select("div[id=head]");
                Elements body_text = document.select("div[id=body-text]");
                Elements footer = document.select("div[id=footer]");
                head = header.toString();
                body = body_text.toString();
                foot = footer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            TextView header_text = (TextView) getSherlockActivity().findViewById(R.id.notification_head);
            TextView body_text = (TextView) getSherlockActivity().findViewById(R.id.notification_body);
            TextView footer_text = (TextView) getSherlockActivity().findViewById(R.id.notification_foot);

            header_text.setText(Html.fromHtml(head));
            body_text.setText(Html.fromHtml(body));
            footer_text.setText(Html.fromHtml(foot));

            mProgressDialog.dismiss();
        }
    }


    private class ContentTR extends AsyncTask<Void, Void, Void>
    {
        @SuppressWarnings("unused")
		String title;
        String head;
        String body;
        String foot;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getSherlockActivity());
            mProgressDialog.setTitle(R.string.progress_lang_tr);
            mProgressDialog.setMessage("Bildirimler web kaynağı yükleniyor");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try{
                // Connecting to the web resource
                Document document = Jsoup.connect(url_page_tr).get();
                title = document.title();
                //Element header = document.getElementById("head");
                Elements header = document.select("div[id=head]");
                Elements body_text = document.select("div[id=body-text]");
                Elements footer = document.select("div[id=footer]");
                head = header.toString();
                body = body_text.toString();
                foot = footer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            TextView header_text = (TextView) getSherlockActivity().findViewById(R.id.notification_head);
            TextView body_text = (TextView) getSherlockActivity().findViewById(R.id.notification_body);
            TextView footer_text = (TextView) getSherlockActivity().findViewById(R.id.notification_foot);

            header_text.setText(Html.fromHtml(head));
            body_text.setText(Html.fromHtml(body));
            footer_text.setText(Html.fromHtml(foot));

            mProgressDialog.dismiss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        _fragmentView = inflater.inflate(R.layout.notification_layout, container, false);

        if (_fragmentView == null)
            return null;

        return _fragmentView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ( item.getTitle().equals(getString(R.string.action_home)) ) {
            returnHome();
            return true;
        } else if (item.getTitle().equals(getString(R.string.action_lang_en))) {
            selectLanguageUK();
            return true;
        } else if (item.getTitle().equals(getString(R.string.action_lang_ru))) {
            selectLanguageRU();
            return true;
        } else if (item.getTitle().equals(getString(R.string.action_lang_kg))) {
            selectLanguageKG();
            return true;
        } else if (item.getTitle().equals(getString(R.string.action_lang_tr))) {
            selectLanguageTR();
            return true;
        }

        return false;
    }

    /**
     * Selecting a language for received content
     */
    private void selectLanguageUK() {
        new ContentEng().execute();
    }

    private void selectLanguageRU() {
        new ContentRU().execute();
    }

    private void selectLanguageKG() {
        new ContentKG().execute();
    }

    private void selectLanguageTR() {
        new ContentTR().execute();
    }

    /**
     * Returning to home.
     */
    private void returnHome()
    {
        Intent intent = new Intent(getActivity(),HomeActivity.class);
        startActivity(intent);
    }
}
