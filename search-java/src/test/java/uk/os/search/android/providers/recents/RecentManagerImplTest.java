/*
 * Copyright (C) 2016 Ordnance Survey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.os.search.android.providers.recents;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import org.junit.Test;
import io.reactivex.observers.TestObserver;
import uk.os.search.SearchResult;

import static org.junit.Assert.assertEquals;

public class RecentManagerImplTest {

    @Test
    public void shouldSaveResultAndAllowQueryWithoutSubscription() throws Exception {
        TestObserver<Void> testSubscriber = new TestObserver<>();

        RecentsManager recentsManager = getRecentsManager();
        recentsManager.saveRecent(createSearchResult("Test1", 10000)).subscribe(testSubscriber);

        testSubscriber.awaitTerminalEvent(3, TimeUnit.SECONDS);
        testSubscriber.assertNoErrors();
        testSubscriber.assertComplete();

        TestObserver<List<SearchResult>> resultsSubscriber = new TestObserver<>();
        recentsManager.last(10).subscribe(resultsSubscriber);
        resultsSubscriber.awaitTerminalEvent(3, TimeUnit.SECONDS);

        List<SearchResult> results = resultsSubscriber.values().get(0);
        int expectedSize = 1;
        assertEquals(expectedSize, results.size());

        final String name = "The Last One";
        recentsManager.saveRecent(createSearchResult(name, 99999));
        // just to prove a direct subscription isn't necessary
        Thread.sleep(1000);

        List<SearchResult> finalResults = recentsManager.query(name).blockingFirst();
        SearchResult finalResult = finalResults.get(0);
        String expectedName = name;
        String actualName = finalResult.getName();
        assertEquals(expectedName, actualName);
        System.out.println(finalResult.getName());
    }

    @Test
    public void shouldSaveResultAndAllowQueryWhenSubscribed() throws Exception {
        TestObserver<Void> saveSubscriber = new TestObserver<>();
        RecentsManager recentsManager = getRecentsManager();
        recentsManager.saveRecent(createSearchResult("Test1", 10000)).subscribe(saveSubscriber);
        saveSubscriber.awaitTerminalEvent(3, TimeUnit.SECONDS);

        TestObserver<List<SearchResult>> testSubscriber = new TestObserver<>();
        recentsManager.last(10).subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(3, TimeUnit.SECONDS);

        testSubscriber.assertNoErrors();

        int expectedEmissions = 1;
        int actualEmissions = testSubscriber.values().size();
        assertEquals(expectedEmissions, actualEmissions);

        List<SearchResult> result = testSubscriber.values().get(0);

        int expectedSize = 1;
        int actualSize = result.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void shouldReturnSingleResponseWithFourSearchResults() throws Exception {
      RecentsManager recentsManager = getRecentsManagerWithResults();

      TestObserver<List<SearchResult>> testSubscriber = recentsManager.last(10).subscribeWith(new TestObserver<List<SearchResult>>());
      testSubscriber.awaitTerminalEvent();
      testSubscriber.assertNoErrors();
      testSubscriber.assertComplete();

      // isDisposed is an unreliable method of establishing whether something has completed
      // See https://github.com/ReactiveX/RxJava/issues/5283
      // assertTrue(testSubscriber.isDisposed());

      int expectedResults = 1;
      int actualResults = testSubscriber.values().size();
      assertEquals(expectedResults, actualResults);

      List<SearchResult> result = testSubscriber.values().get(0);
      int expectedSize = 4;
      assertEquals(expectedSize, result.size());

      // verify order of recents
      assertEquals("Darkwood", result.get(0).getName());
      assertEquals("Cobwood", result.get(1).getName());
      assertEquals("Blackwood", result.get(2).getName());
      assertEquals("Applewood", result.get(3).getName());
    }

    private SearchResult createSearchResult(String name, double position) {
        Envelope envelope = new Envelope(position - 500, position - 500, position + 500, position + 500);
        return new SearchResult(name.toLowerCase().substring(0, 1),
                name,
                String.format("%s Town", name),
                new Point(position, position),
                envelope,
                SpatialReference.create(27700));
    }

    private RecentsManager getRecentsManager() {
        return new RecentsManagerImpl();
    }

    private RecentsManager getRecentsManagerWithResults() throws InterruptedException {
        RecentsManager recentsManager = new RecentsManagerImpl();

        save(createSearchResult("Applewood", 10000), recentsManager);
        save(createSearchResult("Blackwood", 20000), recentsManager);
        save(createSearchResult("Cobwood", 30000), recentsManager);
        save(createSearchResult("Darkwood", 40000), recentsManager);

        return recentsManager;
    }

    private void save(SearchResult searchResult, RecentsManager recentsManager) throws InterruptedException {
        TestObserver<Void> testSubscriber = new TestObserver<>();
        recentsManager.saveRecent(searchResult).subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(3, TimeUnit.SECONDS);
    }
}

