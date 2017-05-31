package at.shockbytes.dante.dagger;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import at.shockbytes.dante.network.BookDownloader;
import at.shockbytes.dante.network.google.gson.BookBackupSerializer;
import at.shockbytes.dante.network.google.gson.GoogleBooksResponseDeserializer;
import at.shockbytes.dante.util.AppParams;
import at.shockbytes.dante.util.DanteRealmMigration;
import at.shockbytes.dante.util.backup.BackupManager;
import at.shockbytes.dante.util.backup.GoogleDriveBackupManager;
import at.shockbytes.dante.util.books.Book;
import at.shockbytes.dante.util.books.BookManager;
import at.shockbytes.dante.util.books.RealmBookManager;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;

/**
 * @author Martin Macheiner
 *         Date: 13.02.2017.
 */

@Module
public class AppModule {

    private Application app;

    public AppModule(Application app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(app.getApplicationContext());
    }

    @Provides
    @Singleton
    public BookManager provideBookManager(BookDownloader bookDownloader, Realm realm) {
        return new RealmBookManager(bookDownloader, realm);
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Book.class, new GoogleBooksResponseDeserializer())
                .create();
    }

    @Provides
    @Singleton
    @Named("backup_gson")
    public Gson provideBackupGson() {
        try {
            return new GsonBuilder()
                    .setExclusionStrategies(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes f) {
                            return f.getDeclaringClass().equals(RealmObject.class);
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false;
                        }
                    })
                    .registerTypeAdapter(Class.forName("io.realm.BookRealmProxy"), new BookBackupSerializer())
                    .create();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new Gson(); // THis should never be the case
    }


    @Provides
    @Singleton
    public Realm provideRealm() {

        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(AppParams.REALM_SCHEMA_VERSION)
                .migration(new DanteRealmMigration())
                .build();
        return Realm.getInstance(config);
    }

    @Provides
    @Singleton
    public BackupManager provideBackupManager(SharedPreferences preferences, @Named("backup_gson") Gson gson) {
        return new GoogleDriveBackupManager(app.getApplicationContext(), preferences, gson);
    }


}
