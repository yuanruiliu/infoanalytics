package utils;

import java.util.List;

import Jama.Matrix;

/**
 * A simple container for the smartvote candidates data, that allows to store:
 *
 * - the N x M matrix of answers for each candidate,
 * - the political party affiliations for each of the N candidates.
 */
public class CandidatesData {

    public final Matrix answersMatrix;
    public final List<String> partyAffiliations;

    public CandidatesData(Matrix answersMatrix, List<String> partyAffiliations) {
        this.answersMatrix = answersMatrix;
        this.partyAffiliations = partyAffiliations;
    }
}
