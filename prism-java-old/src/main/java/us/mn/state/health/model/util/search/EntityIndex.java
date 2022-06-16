package us.mn.state.health.model.util.search;

import java.util.Collection;
import java.io.File;
import java.io.IOException;

import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.commons.logging.Log;
import us.mn.state.health.common.exceptions.InfrastructureException;

public abstract class EntityIndex {
    protected static final int optimizeValue = 1000;
    public abstract void add(Searchable entity) throws InfrastructureException;

    public abstract void drop(Searchable entity) throws InfrastructureException;

    public abstract void dropAndAdd(Searchable entity) throws InfrastructureException;

    public abstract Collection search(String queryString) throws InfrastructureException;

    public abstract Collection searchIds(String queryString) throws InfrastructureException;

    public abstract Collection search(Query query) throws InfrastructureException;

    public abstract Collection searchIds(Query query) throws InfrastructureException;


    protected static void addHandler(File file, Document document, Analyzer analyzer, Log log, boolean optimize) throws IOException {
        long t1 = System.currentTimeMillis();
        if (unlockDirectory(file)) {
            log.error("On Add() unlocked file " + file.getAbsolutePath());
        }
        IndexWriter rliIndexWriter = new IndexWriter(file, analyzer, false);
        try {
            rliIndexWriter.addDocument(document);
            if (optimize) {
                rliIndexWriter.optimize();
            }
        }
        finally {
            rliIndexWriter.close();
        }
    }


    protected static void dropHandler(File file, String field, String value, Log log) throws IOException {
        if (unlockDirectory(file)) {
            log.error("On Drop() unlocked file " + file.getAbsolutePath());
        }
        final IndexReader reader = IndexReader.open(file);
        try {
            Term term = new Term(field, value);
            reader.deleteDocuments(term);
        }
        finally {
            reader.close();
        }
    }

    private static boolean unlockDirectory(File file) throws IOException {
        FSDirectory directory = FSDirectory.getDirectory(file, false);
        if (IndexReader.isLocked(directory)) {
            IndexReader.unlock(directory);
            return true;
        }
        return false;
    }
}