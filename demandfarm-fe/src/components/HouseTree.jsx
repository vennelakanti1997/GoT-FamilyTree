import { useCallback, useState } from "react";
import { Col, Container, Image, Row } from "react-bootstrap";
import Tree from "react-d3-tree";
import { CharacterCard } from "./CharacterCard";

const useCenteredTree = (defaultTranslate = { x: 0, y: 0 }) => {
  const [translate, setTranslate] = useState(defaultTranslate);
  const [dimensions, setDimensions] = useState();
  const containerRef = useCallback((containerElem) => {
    if (containerElem !== null) {
      const { width, height } = containerElem.getBoundingClientRect();
      setDimensions({ width, height });
      setTranslate({ x: width / 2, y: height / 2 });
    }
  }, []);
  return [dimensions, translate, containerRef];
};
const TreeComponent = ({ houseMembers, setHouseMembers, houseName }) => {
  const [dimensions, translate, containerRef] = useCenteredTree();
  const [openCard, setOpenCard] = useState(false);
  const [characterNameSelected, setSelectedCharacterName] = useState("");

  const handleNodeClick = (name) => {
    setOpenCard(true);
    setSelectedCharacterName(name);
  };

  const renderRectSvgNode = ({ nodeDatum }) => (
    <>
     
      <g>
        <rect
          width="20"
          height="20"
          x="-10"
          fill={nodeDatum.attributes?.favoriteId ? "green" : "white"}
          onClick={() => handleNodeClick(nodeDatum.name)}
        />

        <text fill="black" strokeWidth="1" x="20">
          {nodeDatum.name}
        </text>
        {nodeDatum.attributes?.spouse && (
          <text fill="red" x="20" dy="20" strokeWidth="1">
            spouse: {nodeDatum.attributes?.spouse}
          </text>
        )}
      </g>
    </>
  );
  const closeCard = () => {
    setOpenCard(false);
    setSelectedCharacterName("");
  };

  return (
    <Container fluid className="d-flex vh-100">
      <Row className="m-auto align-self-center">
        {openCard && characterNameSelected ? (
          <CharacterCard
            characterName={characterNameSelected}
            houseName={houseName}
            closeCard={closeCard}
            setHouseMembers={setHouseMembers}
          />
        ) : (
          <Col
            id={`${houseMembers.name}-tree`}
            key={houseMembers.name}
            style={{
              width: "100vw",
              height: "80vh",
            }}
            xs={12}
            md={8}
            ref={containerRef}
          >
            <Tree
              dataKey={houseMembers.name}
              dimensions={dimensions}
              translate={translate}
              data={houseMembers}
              pathFunc="straight"
              orientation="vertical"
              zoomable={false}
              renderCustomNodeElement={renderRectSvgNode}
            />
          </Col>
        )}
      </Row>
    </Container>
  );
};

export const HouseTree = ({ houseMembers, houseName, setHouseMembers }) => {
  return (
    <>
      {houseName ? (
        <TreeComponent
          houseMembers={houseMembers}
          setHouseMembers={setHouseMembers}
          houseName={houseName}
        />
      ) : (
        <div className="text-danger">Please Select a house</div>
      )}
    </>
  );
};
