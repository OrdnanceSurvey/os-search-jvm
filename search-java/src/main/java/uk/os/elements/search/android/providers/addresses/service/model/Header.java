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

package uk.os.elements.search.android.providers.addresses.service.model;

import java.io.Serializable;

/**
 * generated code
 */
public class Header implements Serializable {
    private String dataset;

    private String epoch;

    private String format;

    private String lr;

    private String matchprecision;

    private String maxresults;

    private String offset;

    private String output_srs;

    private String query;

    private String totalresults;

    private String uri;

    public String getDataset() {
        return dataset;
    }

    public String getEpoch() {
        return epoch;
    }

    public String getFormat() {
        return format;
    }

    public String getLr() {
        return lr;
    }

    public String getMatchprecision() {
        return matchprecision;
    }

    public String getMaxresults() {
        return maxresults;
    }

    public String getOffset() {
        return offset;
    }

    public String getOutput_srs() {
        return output_srs;
    }

    public String getQuery() {
        return query;
    }

    public String getTotalresults() {
        return totalresults;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return "ClassPojo [maxresults = " + maxresults + ", lr = " + lr + ", dataset = " + dataset + ", query = " + query + ", epoch = " + epoch + ", matchprecision = " + matchprecision + ", output_srs = " + output_srs + ", format = " + format + ", totalresults = " + totalresults + ", offset = " + offset + ", uri = " + uri + "]";
    }
}