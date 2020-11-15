package parchments_backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import parchments_backend.domain.BreadcrumbList;
import parchments_backend.domain.Parchment;
import java.util.List;
import java.util.Optional;

public interface ParchmentRepository extends Neo4jRepository<Parchment, Long> {

    @Query("MATCH (w:Writer) WHERE id(w) = $writerId CREATE (w)-[:WROTE]->(p:Parchment{title:$s.title, synopsis:$s.synopsis, contents:$s.contents}) RETURN p")
    Parchment save(Parchment s, Long writerId);

    @Query("MATCH (w:Writer) WHERE id(w) = $writerId MATCH (pre:Parchment) WHERE id(pre) = $previousParchmentId CREATE (w)-[:WROTE]->(p:Parchment{title:$s.title, synopsis:$s.synopsis, contents:$s.contents})<-[:CONTINUATION]-(pre) RETURN p")
    Parchment save(Parchment s, Long writerId, Long previousParchmentId);

    @Query("MATCH (w:Writer)-[wr:WROTE]->(p:Parchment) WHERE id(w) = $writerId return p")
    List<Parchment> findAllByWriterId(Long writerId);

    @Query("CALL db.index.fulltext.queryNodes(\"parchmentTitles\", $title + \"~\") YIELD node\n" +
            "RETURN id(node)")
    List<Long> findAllIdsByTitle(String title);

    @Query("MATCH (p:Parchment) WHERE id(p) IN $ids WITH p AS parchment, (p)<-[:VOTED]-(:Writer) as votes ORDER BY size(votes) DESC RETURN parchment")
    List<Parchment> findAllByMostVoted(List<Long> ids);

    @Query("MATCH (p:Parchment) WHERE id(p) IN $ids RETURN p ORDER BY p.title")
    List<Parchment> findAllByAlphabetic(List<Long> ids);

    @Query(value="MATCH (p:Parchment) WHERE NOT (p)<-[:CONTINUATION]-(:Parchment) RETURN p",
            countQuery = "MATCH (p:Parchment) WHERE NOT (p)<-[:CONTINUATION]-(:Parchment) RETURN count(p)")
    Page<Parchment> findCoreParchments(Pageable pageable);

    @Query("MATCH (p:Parchment) WHERE NOT (p)<-[:CONTINUATION]-(:Parchment) RETURN p, rand() as r ORDER BY r LIMIT 1")
    Optional<Parchment> findRandomCoreParchment();

    Optional<Parchment> findById(Long id);

    @Query("MATCH path=(p:Parchment)<-[:CONTINUATION*]-(pre:Parchment) WHERE ID(p) = $id AND NOT (pre)<-[:CONTINUATION]-() RETURN reverse([node in nodes(path)| {title: node.title, id: id(node)}]) as list")
    BreadcrumbList findBreadcrumbs(Long id);

    Optional<Parchment> findByTitle(String title);

    List<Parchment> findAll();

    @Query(value="MATCH (pre:Parchment)-[:CONTINUATION]->(p:Parchment) WHERE id(pre) = $id RETURN p ORDER BY p.title",
            countQuery = "MATCH (pre:Parchment)-[:CONTINUATION]->(p:Parchment) WHERE id(pre) = $id RETURN count(p)")
    Page<Parchment> findContinuationsByAlphabetic(Pageable pageable, Long id);

    @Query(value="MATCH (pre:Parchment)-[:CONTINUATION]->(p:Parchment) WHERE id(pre) = $id WITH p AS parchment, (p)<-[:VOTED]-(:Writer) as votes ORDER BY size(votes) DESC RETURN parchment",
            countQuery = "MATCH (pre:Parchment)-[:CONTINUATION]->(p:Parchment) WHERE id(pre) = $id RETURN count(p)")
    Page<Parchment> findContinuationsByMostVoted(Pageable pageable, Long id);

    @Query("MATCH (p:Parchment), (w:Writer) WHERE id(p) = $parchmentId AND id(w) = $writerId CREATE (w)-[:VOTED {writerId: id(w)}]->(p)")
    void voteParchment(Long writerId, Long parchmentId);

    @Query("MATCH (p:Parchment)<-[v:VOTED]-(:Writer) WHERE id(p) = $parchmentId RETURN count(v)")
    Integer getVoteCount(Long parchmentId);

    @Query("MATCH (p:Parchment), (w:Writer) WHERE id(p) = $id AND id(w) = $readerId RETURN EXISTS( (w)-[:VOTED]->(p) )")
    boolean getReaderVoted(Long readerId, Long id);

    @Query("MATCH (p:Parchment)<-[v:VOTED]-(w:Writer) WHERE id(p) = $parchmentId AND id(w) = $writerId DELETE v")
    void cancelVoteParchment(Long writerId, Long parchmentId);
}
