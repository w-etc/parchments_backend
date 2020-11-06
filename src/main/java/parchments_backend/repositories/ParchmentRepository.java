package parchments_backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import parchments_backend.domain.Breadcrumb;
import parchments_backend.domain.BreadcrumbList;
import parchments_backend.domain.Parchment;
import java.util.List;
import java.util.Optional;

public interface ParchmentRepository extends Neo4jRepository<Parchment, Long> {

    @Query("MATCH (w:Writer) WHERE id(w) = $writerId CREATE (w)-[:WROTE]->(p:Parchment{title:$title, synopsis:$synopsis, contents:$contents}) RETURN p")
    Parchment save(String title, String synopsis, String contents, Long writerId);

    @Query("MATCH (w:Writer) WHERE id(w) = $writerId MATCH (pre:Parchment) WHERE id(pre) = $previousParchmentId CREATE (w)-[:WROTE]->(p:Parchment{title:$title, synopsis:$synopsis, contents:$contents})<-[:CONTINUATION]-(pre) RETURN p")
    Parchment save(String title, String synopsis, String contents, Long writerId, Long previousParchmentId);

    @Query("MATCH (w:Writer)-[wr:WROTE]->(p:Parchment) WHERE id(w) = $writerId return p")
    List<Parchment> findAllByWriterId(Long writerId);

    @Query("CALL db.index.fulltext.queryNodes(\"parchmentTitles\", $title + \"~\") YIELD node\n" +
            "RETURN node")
    List<Parchment> findAllByTitle(String title);

    @Query(value="MATCH (p:Parchment) WHERE NOT (p)<-[:CONTINUATION]-(:Parchment) RETURN p",
            countQuery = "MATCH (p:Parchment) WHERE NOT (p)<-[:CONTINUATION]-(:Parchment) RETURN count(p)")
    Page<Parchment> findCoreParchments(Pageable pageable);

    @Query("MATCH (p:Parchment) WHERE NOT (p)<-[:CONTINUATION]-(:Parchment) RETURN p, rand() as r ORDER BY r LIMIT 1")
    Optional<Parchment> findRandomCoreParchment();

    Optional<Parchment> findById(Long id);

    @Query("MATCH (p:Parchment) WHERE id(p) = $id RETURN p")
    Optional<Parchment> findAsd(Long id);

    @Query("MATCH path=(p:Parchment)<-[:CONTINUATION*]-(pre:Parchment) WHERE ID(p) = $id AND NOT (pre)<-[:CONTINUATION]-() RETURN reverse([node in nodes(path)| {title: node.title, id: id(node)}]) as list")
    List<Breadcrumb> findBreadcrumbs(Long id);

    Optional<Parchment> findByTitle(String title);

    List<Parchment> findAll();

    @Query(value="MATCH (pre:Parchment)-[:CONTINUATION]->(p:Parchment) WHERE id(pre) = $id RETURN p",
            countQuery = "MATCH (pre:Parchment)-[:CONTINUATION]->(p:Parchment) WHERE id(pre) = $id RETURN count(p)")
    Page<Parchment> findContinuationsById(Pageable pageable, Long id);
}
