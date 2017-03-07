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
public class NewsFragment extends SherlockFragment
{
    private View _fragmentView;
    private String server = LoginActivity.local_address;
    private String url_page_en = server + "//UIMS/Help/news/en.html";
    private String url_page_ru = server + "//UIMS/Help/news/ru.html";
    private String url_page_kg = server + "//UIMS/Help/news/kg.html";
    private String url_page_tr = server + "//UIMS/Help/news/tr.html";
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
//      a fragment wont receive the callbacks
//      when Action Bar items are selected.
        setHasOptionsMenu(true);

        new ContentEng().execute();
    }

    private class ContentEng extends AsyncTask<Void, Void, Void>
    {
        //String title;
        //String head;
        String body;
        //String foot;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getSherlockActivity());
            mProgressDialog.setTitle(R.string.progress_lang_en);
            mProgressDialog.setMessage("News web content is loading..");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try{
                // Connecting to the web resource
                Document document = Jsoup.connect(url_page_en).get();
               // title = document.title();
                //Element header = document.getElementById("head");
                //Elements header = document.select("div[id=head]");
                Elements body_text = document.select("div[id=body-text]");
                //Elements footer = document.select("div[id=footer]");
                //head = header.toString();
                body = body_text.toString();
                //foot = footer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //TextView header_text = (TextView) getSherlockActivity().findViewById(R.id.head);
            TextView body_text = (TextView) getSherlockActivity().findViewById(R.id.news_body);
            //TextView footer_text = (TextView) getSherlockActivity().findViewById(R.id.foot);

            //header_text.setText(Html.fromHtml(head));
            body_text.setText(Html.fromHtml(body));
            //footer_text.setText(Html.fromHtml(foot));

            mProgressDialog.dismiss();
        }
    }

    private class ContentRu extends AsyncTask<Void, Void, Void>
    {
       // String title;
        //String head;
        String body;
        //String foot;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getSherlockActivity());
            mProgressDialog.setTitle(R.string.progress_lang_ru);
            mProgressDialog.setMessage("веб-контент загружается..");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try{
                // Connecting to the web resource
                Document document = Jsoup.connect(url_page_ru).get();
          //      title = document.title();
                //Element header = document.getElementById("head");
                //Elements header = document.select("div[id=head]");
                Elements body_text = document.select("div[id=body-text]");
                //Elements footer = document.select("div[id=footer]");
                //head = header.toString();
                body = body_text.toString();
                //foot = footer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //TextView header_text = (TextView) getSherlockActivity().findViewById(R.id.head);
            TextView body_text = (TextView) getSherlockActivity().findViewById(R.id.news_body);
            //TextView footer_text = (TextView) getSherlockActivity().findViewById(R.id.foot);

            //header_text.setText(Html.fromHtml(head));
            body_text.setText(Html.fromHtml(body));
            //footer_text.setText(Html.fromHtml(foot));

            mProgressDialog.dismiss();
        }
    }

    private class ContentKg extends AsyncTask<Void, Void, Void>
    {
       // String title;
        //String head;
        String body;
        //String foot;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getSherlockActivity());
            mProgressDialog.setTitle(R.string.progress_lang_kg);
            mProgressDialog.setMessage("веб-контент загружается..");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try{
                // Connecting to the web resource
                Document document = Jsoup.connect(url_page_kg).get();
              //  title = document.title();
                //Element header = document.getElementById("head");
                //Elements header = document.select("div[id=head]");
                Elements body_text = document.select("div[id=body-text]");
                //Elements footer = document.select("div[id=footer]");
                //head = header.toString();
                body = body_text.toString();
                //foot = footer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //TextView header_text = (TextView) getSherlockActivity().findViewById(R.id.head);
            TextView body_text = (TextView) getSherlockActivity().findViewById(R.id.news_body);
            //TextView footer_text = (TextView) getSherlockActivity().findViewById(R.id.foot);

            //header_text.setText(Html.fromHtml(head));
            body_text.setText(Html.fromHtml(body));
            //footer_text.setText(Html.fromHtml(foot));

            mProgressDialog.dismiss();
        }
    }

    private class ContentTr extends AsyncTask<Void, Void, Void>
    {
       // String title;
        //String head;
        String body;
        //String foot;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getSherlockActivity());
            mProgressDialog.setTitle(R.string.progress_lang_tr);
            mProgressDialog.setMessage("News web kaynağı yükleniyor..");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try{
                // Connecting to the web resource
                Document document = Jsoup.connect(url_page_tr).get();
             //   title = document.title();
                //Element header = document.getElementById("head");
                //Elements header = document.select("div[id=head]");
                Elements body_text = document.select("div[id=body-text]");
                //Elements footer = document.select("div[id=footer]");
                //head = header.toString();
                body = body_text.toString();
                //foot = footer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //TextView header_text = (TextView) getSherlockActivity().findViewById(R.id.head);
            TextView body_text = (TextView) getSherlockActivity().findViewById(R.id.news_body);
            //TextView footer_text = (TextView) getSherlockActivity().findViewById(R.id.foot);

            //header_text.setText(Html.fromHtml(head));
            body_text.setText(Html.fromHtml(body));
            //footer_text.setText(Html.fromHtml(foot));

            mProgressDialog.dismiss();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        _fragmentView = inflater.inflate(R.layout.news_layout, container, false);

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
        new ContentRu().execute();
    }

    private void selectLanguageKG() {
        new ContentKg().execute();
    }

    private void selectLanguageTR() { new ContentTr().execute(); }


    /**
     * Returning to home.
     */
    private void returnHome()
    {
        Intent intent = new Intent(getActivity(),HomeActivity.class);
        startActivity(intent);
    }

}
