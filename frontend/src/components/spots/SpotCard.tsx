import React, { useContext, useState } from 'react';
import { Card, Box, CardMedia, Typography, Stack, Rating, IconButton } from '@mui/material';
import { ExpandMoreRounded } from '@mui/icons-material';
import Btn_Add from '../Btn_Add';
import Tag_Category from '../Tag_Category';
import Tag_IsFree from '../Tag_IsFree';
import { ListContext } from '../../contexts/ListContext';
import { useResponsiveCardWidth } from '../../utils/useResponsiveSizes';
import { useAlertContext } from '../../contexts/AlertContext';

interface SpotCardProps {
  id: string;
  image1: string;
  image3: string;
  title: string;
  address: string;
  rating: number;
  isFree: boolean;
  category: string;
  user_ratings_total: number;
  onExpand: () => void;
  onHover: () => void;
  onLeave: () => void;
}

const SpotCard: React.FC<SpotCardProps> = ({
  id, image1, image3, title, isFree, rating, category, user_ratings_total, onExpand, onHover, onLeave
}) => {
  const [currentImage, setCurrentImage] = useState(image1);
  const [imageStyle, setImageStyle] = useState({});
  const { addItemWithDateCheck, isItemInList } = useContext(ListContext);
  const { openMissingDatesAlert } = useAlertContext();

  const handleAdd = () => {
    const spotData = { id, title, image: image1 };
    addItemWithDateCheck(spotData, openMissingDatesAlert, 'SpotCard');
  };

  const isAdded = isItemInList(title);
  const cardWidth = useResponsiveCardWidth();

  return (
    <Card
      sx={{ borderRadius: '8px', overflow: 'hidden', boxShadow: '0px 0px 6px rgba(0, 0, 0, 0.2)', width: cardWidth, marginBottom: 2, gap: 1, height: "300px", ':hover': { cursor: 'pointer', boxShadow: '0px 0px 12px rgba(0, 0, 0, 0.4)' } }}
      onMouseEnter={() => { setCurrentImage(image3); setImageStyle({ transition: 'transform 7s ease', transform: 'scale(1.4)' }); onHover(); }}
      onMouseLeave={() => { setCurrentImage(image1); setImageStyle({ transition: 'none', transform: 'scale(1)' }); onLeave(); }}
    >
      <Box sx={{ position: 'relative', overflow: 'hidden' }}>
        <CardMedia component="img" height="170" image={currentImage} alt={title} sx={{ ...imageStyle }} />
      </Box>
      <Stack sx={{ paddingTop: '8px', paddingLeft: '12px' }} gap="2px">
        <Typography sx={{ fontSize: '1.1rem', fontWeight: 400, overflow: 'hidden', whiteSpace: 'nowrap', textOverflow: 'ellipsis', maxWidth: '95%', marginLeft: '2px' }}>
          {title}
        </Typography>
        <Stack direction="row" gap={1}>
          <Rating name="half-rating-read" defaultValue={rating} precision={0.1} readOnly sx={{ fontSize: '1.2rem' }} />
          {rating}
          <Typography sx={{ display: { xs: 'inline', md: 'none', lg: 'inline' }, color: '#888', fontSize: '13px' }}>
            by {user_ratings_total} users
          </Typography>
        </Stack>
        <Stack direction="row">
          <Tag_Category category={category} />
          {isFree && <Tag_IsFree />}
        </Stack>
        <Stack direction="row" justifyContent="space-between" sx={{ width: '95%', paddingTop: 1.5 }}>
          <Btn_Add onClick={handleAdd} isAdded={isAdded} />
          <IconButton onClick={onExpand}><ExpandMoreRounded /></IconButton>
        </Stack>
      </Stack>
    </Card>
  );
};

export default SpotCard;
